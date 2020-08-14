package com.erc.log.containers;

import android.os.Build;

import com.erc.dal.Entity;
import com.erc.dal.Field;
import com.erc.dal.PrimaryKey;
import com.erc.dal.Table;
import com.erc.log.AppContext;
import com.erc.log.configuration.Level;
import com.erc.log.helpers.AndroidId;
import com.erc.log.helpers.ApplicationInformation;
import com.erc.log.helpers.Battery;
import com.erc.log.helpers.Display;
import com.erc.log.helpers.Location;
import com.erc.log.helpers.MemoryInformation;
import com.erc.log.helpers.Network;
import com.erc.log.helpers.Root;

@Table
public class LOG extends Entity {

    @PrimaryKey
    @Field
    public long id;

    @Field
    public long date;

    @Field
    public String deviceId;

    @Field
    public int level;

    @Field
    public String tag;

    @Field
    public String message;

    @Field
    public int count;

    @Field
    public String packageName;

    @Field
    public String appName;

    @Field
    public String version;

    @Field
    public String manufacturer;

    @Field
    public String brand;

    @Field
    public String model;

    @Field
    public String totalMemoryRam;

    @Field
    public String ramMemoryUsage;

    @Field
    public String totalInternalMemory;

    @Field
    public String internalMemoryAvailable;

    @Field
    public String androidVersion;

    @Field
    public String rootedState;

    @Field
    public String locale;

    @Field
    public String batteryLevel;

    @Field
    public String chargingState;

    @Field
    public String networkType;

    @Field
    public String networkState;

    @Field
    public String activeScreen;

    @Field
    public String density;

    @Field
    public String dpi;

    @Field
    public String resolution;

    @Field
    public String orientation;

    public LOG() {
    }

    public LOG(Level level, String tag, String message) {
        this.level = level.value();
        this.tag = tag;
        this.message = message;
        this.date = System.currentTimeMillis();
        this.deviceId = AndroidId.getUniquePseudoID(AppContext.getContext());
        this.count = 1;
        this.packageName = ApplicationInformation.getPackageName(AppContext.getContext());
        try {
            this.appName = ApplicationInformation.getApplicationName(AppContext.getContext());
        } catch (Exception e) {
            //needed for tests
        }
        this.version = ApplicationInformation.getApplicationVersion(AppContext.getContext());
        this.manufacturer = Build.MANUFACTURER;
        this.brand = Build.BRAND;
        this.model = Build.MODEL;
        this.totalMemoryRam = MemoryInformation.getTotalMemory(AppContext.getContext());
        this.ramMemoryUsage = MemoryInformation.getMemoryUsage(AppContext.getContext());
        this.totalInternalMemory = MemoryInformation.getTotalInternalMemorySize();
        this.internalMemoryAvailable = MemoryInformation.getAvailableInternalMemorySize();
        this.androidVersion = Build.VERSION.RELEASE;
        this.rootedState = Root.isDeviceRooted() + "";
        this.locale = Location.getCurrentLocale(AppContext.getContext()).getDisplayName();
        this.batteryLevel = Battery.getBatteryPercentage(AppContext.getContext());
        this.chargingState = Battery.isPlugged(AppContext.getContext());
        this.networkType = Network.getConnectionType(AppContext.getContext()).value();
        this.networkState = Network.connectivityString(AppContext.getContext());
        this.activeScreen = Display.getScreenStatus(AppContext.getContext()).value();
        this.density = Display.getDensityName(AppContext.getContext());
        this.dpi = Display.getDpi(AppContext.getContext());
        this.resolution = Display.getScreenResolution(AppContext.getContext());
        this.orientation = Display.getOrientation(AppContext.getContext());

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level.value();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTotalMemoryRam() {
        return totalMemoryRam;
    }

    public void setTotalMemoryRam(String totalMemoryRam) {
        this.totalMemoryRam = totalMemoryRam;
    }

    public String getRamMemoryUsage() {
        return ramMemoryUsage;
    }

    public void setRamMemoryUsage(String ramMemoryUsage) {
        this.ramMemoryUsage = ramMemoryUsage;
    }

    public String getTotalInternalMemory() {
        return totalInternalMemory;
    }

    public void setTotalInternalMemory(String totalInternalMemory) {
        this.totalInternalMemory = totalInternalMemory;
    }

    public String getInternalMemoryAvailable() {
        return internalMemoryAvailable;
    }

    public void setInternalMemoryAvailable(String internalMemoryAvailable) {
        this.internalMemoryAvailable = internalMemoryAvailable;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getRootedState() {
        return rootedState;
    }

    public void setRootedState(String rootedState) {
        this.rootedState = rootedState;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(String batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getChargingState() {
        return chargingState;
    }

    public void setChargingState(String chargingState) {
        this.chargingState = chargingState;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getNetworkState() {
        return networkState;
    }

    public void setNetworkState(String networkState) {
        this.networkState = networkState;
    }

    public String getActiveScreen() {
        return activeScreen;
    }

    public void setActiveScreen(String activeScreen) {
        this.activeScreen = activeScreen;
    }

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", date=" + date +
                ", deviceId='" + deviceId + '\'' +
                ", level=" + level +
                ", tag='" + tag + '\'' +
                ", message='" + message + '\'' +
                ", count=" + count +
                ", packageName='" + packageName + '\'' +
                ", appName='" + appName + '\'' +
                ", version='" + version + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", totalMemoryRam='" + totalMemoryRam + '\'' +
                ", ramMemoryUsage='" + ramMemoryUsage + '\'' +
                ", totalInternalMemory='" + totalInternalMemory + '\'' +
                ", internalMemoryAvailable='" + internalMemoryAvailable + '\'' +
                ", androidVersion='" + androidVersion + '\'' +
                ", rootedState='" + rootedState + '\'' +
                ", locale='" + locale + '\'' +
                ", batteryLevel='" + batteryLevel + '\'' +
                ", chargingState='" + chargingState + '\'' +
                ", networkType='" + networkType + '\'' +
                ", networkState='" + networkState + '\'' +
                ", activeScreen='" + activeScreen + '\'' +
                ", density='" + density + '\'' +
                ", dpi='" + dpi + '\'' +
                ", resolution='" + resolution + '\'' +
                ", orientation='" + orientation + '\'' + "\n";
    }
}
