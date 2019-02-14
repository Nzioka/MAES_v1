package com.example.mutuaj.helloworld.api
import com.squareup.moshi.Json


data class CropResponse(
    @Json(name = "Crop")
    val crop: String,
    @Json(name = "Drought_tolerance")
    val droughtTolerance: String,
    @Json(name = "Grain_yield")
    val grainYield: String,
    @Json(name = "Licensee")
    val licensee: String,
    @Json(name = "Maintainer_and_seed_source")
    val maintainerAndSeedSource: String,
    @Json(name = "Maturity")
    val maturity: String,
    @Json(name = "Maximum_Altitude")
    val maximumAltitude: Int,
    @Json(name = "Maximum_LGP")
    val maximumLGP: Int,
    @Json(name = "Minimum_Altitude")
    val minimumAltitude: Int,
    @Json(name = "Minimum_LGP")
    val minimumLGP: Int,
    @Json(name = "Release_Name")
    val releaseName: String,
    @Json(name = "Special_attributes")
    val specialAttributes: String,
    @Json(name = "Variety_Name")
    val varietyName: String,
    @Json(name = "Year_of_release_Kenya")
    val yearOfReleaseKenya: Int
)