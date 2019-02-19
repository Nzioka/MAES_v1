package com.example.mutuaj.helloworld.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.mutuaj.helloworld.MaesApplication
import com.example.mutuaj.helloworld.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.HttpException
import java.net.HttpURLConnection


class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocation: Location? = null
    private var mLocationCallback: LocationCallback? = null
    private var mLocationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null

    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */

    private var locationManager: LocationManager? = null

    private val LOCATION_REQUEST_CODE = 999

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val repo get() = MaesApplication.appComponent.getCropRepository()

    private val isLocationEnabled: Boolean
        get() {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_INTERVAL)

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {

                for (location in locationResult?.locations!!) {
                    longitude_value?.text = location.longitude.toString()
                    latitude_value?.text = location.latitude.toString()
                    altitude_value?.text = location.altitude.toString()
                }
            }
        }
        btn_locate_me.setOnClickListener {
            Toast.makeText(this, "button clicked", Toast.LENGTH_LONG).show()
        }

        btnSearchCrops.setOnClickListener {
            postToServer()
        }
        checkLocation()
    }

    private fun checkLocation(): Boolean {
        if (!isLocationEnabled) {
            showAlert()
        }

        return isLocationEnabled
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
            .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
            .setPositiveButton("Location Settings") { _, _ ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Cancel") { _, _ -> }
        dialog.show()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    private fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {

        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                mLocation = it
                longitude_value?.let { it2 ->
                    it2.text = it.longitude.toString()
                }

                latitude_value?.let { it2 ->
                    it2.text = it.latitude.toString()
                }

                altitude_value?.let { it2 ->
                    it2.text = it.altitude.toString()

                }
            }
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }


    override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            startLocationUpdates()
        } else {
            getLastLocation()
            startLocationUpdates()
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient!!.connect()
        }
    }

    override fun onPause() {
        stopLocationUpdates()
        super.onPause()

    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.disconnect()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                Log.d("MainActivity", "Show rationale")
            }
        }

    }

    override fun onConnected(p0: Bundle?) {
        if (mLocation == null) {
            startLocationUpdates()
        }

        if (mLocation != null) {
            mLocation?.let {
                longitude_value?.text = it.longitude.toString()
                latitude_value?.text = it.latitude.toString()
                altitude_value?.text = it.altitude.toString()
            }
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient?.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Toast.makeText(this, "Connection failed ${connectionResult.errorCode}", Toast.LENGTH_LONG).show()
    }

    override fun onLocationChanged(location: Location?) {
        location?.let {
            longitude_value?.text = it.longitude.toString()
            latitude_value?.text = it.latitude.toString()
            altitude_value?.text = it.altitude.toString()
        }
    }

    fun postToServer() {

        var lon = longitude_value.text.toString()
        var lat = latitude_value.text.toString()

        var maturity = maturity_spinner.selectedItem.toString()
        var drought = drought_resistance_spinner.selectedItem.toString()

        Log.d("MAIN", "$lon, $lat $maturity, $drought")

        repo.postToServer(lat, lon, "Maize", maturity, drought)
            .toFlowable()
            .flatMap {
                Flowable.fromIterable(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // for now just logging it to console
                Log.d("MAIN", "$it")
            }, {
                processException(it)
            })


    }

    private fun processException(throwable: Throwable) {

        if (throwable is HttpException) {

            when (throwable.code()) {

                HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                    Log.d("MAIN", "Internal server error")
                }

                HttpURLConnection.HTTP_NOT_FOUND -> {
                    Log.d("MAIN", "Not found")
                }

                HttpURLConnection.HTTP_BAD_REQUEST -> {
                    Log.d("MAIN", "Bad Request")
                }
            }
        }
    }
}
