package org.sap.wenjun.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonPropertyOrder({
        "FID", "RESTROOMS", "PICNIC", "FISHING", "AKA", "AccessType", "AccessID",
        "Class", "Address", "Fee", "BikeRack", "BikeTrail", "DogTube",
        "Grills", "TrashCans", "ParkSpaces", "ADAsurface", "ADAtoilet",
        "ADAfishing", "ADAcamping", "ADApicnic", "ADAtrail", "ADAparking",
        "ADAfacilit", "ADAfacName", "HorseTrail", "DateFrom", "DateTo",
        "RecycleBin", "DogCompost", "AccessName", "THLeash"
})
public class TrailInfo {
    @JsonProperty("FID")
    private int fid;

    @JsonProperty("RESTROOMS")
    private String restrooms;

    @JsonProperty("PICNIC")
    private String picnic;

    @JsonProperty("FISHING")
    private String fishing;

    @JsonProperty("AKA")
    private String aka;

    @JsonProperty("AccessType")
    private String accessType;

    @JsonProperty("AccessID")
    private String accessID;

    @JsonProperty("Class")
    private String trailClass;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Fee")
    private String fee;

    @JsonProperty("BikeRack")
    private String bikeRack;

    @JsonProperty("BikeTrail")
    private String bikeTrail;

    @JsonProperty("DogTube")
    private String dogTube;

    @JsonProperty("Grills")
    private String grills;

    @JsonProperty("TrashCans")
    private int trashCans;

    @JsonProperty("ParkSpaces")
    private int parkSpaces;

    @JsonProperty("ADAsurface")
    private String adaSurface;

    @JsonProperty("ADAtoilet")
    private String adaToilet;

    @JsonProperty("ADAfishing")
    private String adaFishing;

    @JsonProperty("ADAcamping")
    private String adaCamping;

    @JsonProperty("ADApicnic")
    private String adaPicnic;

    @JsonProperty("ADAtrail")
    private String adaTrail;

    @JsonProperty("ADAparking")
    private String adaParking;

    @JsonProperty("ADAfacilit")
    private String adaFacility;

    @JsonProperty("ADAfacName")
    private String adaFacilityName;

    @JsonProperty("HorseTrail")
    private String horseTrail;

    @JsonProperty("DateFrom")
    private String dateFrom;

    @JsonProperty("DateTo")
    private String dateTo;

    @JsonProperty("RecycleBin")
    private String recycleBin;

    @JsonProperty("DogCompost")
    private String dogCompost;

    @JsonProperty("AccessName")
    private String accessName;

    @JsonProperty("THLeash")
    private String thLeash;
}
