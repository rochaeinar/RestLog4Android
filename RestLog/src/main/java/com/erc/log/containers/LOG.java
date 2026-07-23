package com.erc.log.containers;

import com.erc.dal.Entity;
import com.erc.dal.Field;
import com.erc.dal.PrimaryKey;
import com.erc.dal.Table;
import com.erc.log.AppContext;
import com.erc.log.configuration.Level;
import com.erc.log.format.LogFormatter;
import com.erc.log.helpers.DeviceInfoCache;

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
        this.tag = sanitize(tag);
        this.message = sanitize(message);
        this.date = System.currentTimeMillis();
        this.count = 1;

        // Device/app info is not gathered here anymore: constant values are captured once
        // per process and dynamic ones (memory, battery, network…) are read from a short
        // TTL cache, so logging thousands of records no longer re-queries the system each
        // time. See DeviceInfoCache.
        DeviceInfoCache info = DeviceInfoCache.getInstance(AppContext.getContext());
        this.deviceId = info.deviceId;
        this.packageName = info.packageName;
        this.appName = info.appName;
        this.version = info.version;
        this.manufacturer = info.manufacturer;
        this.brand = info.brand;
        this.model = info.model;
        this.totalMemoryRam = info.totalMemoryRam;
        this.totalInternalMemory = info.totalInternalMemory;
        this.androidVersion = info.androidVersion;
        this.rootedState = info.rootedState;
        this.locale = info.locale;
        this.density = info.density;
        this.dpi = info.dpi;
        this.resolution = info.resolution;

        DeviceInfoCache.Dynamic dynamic = info.getDynamic();
        this.ramMemoryUsage = dynamic.ramMemoryUsage;
        this.internalMemoryAvailable = dynamic.internalMemoryAvailable;
        this.batteryLevel = dynamic.batteryLevel;
        this.chargingState = dynamic.chargingState;
        this.networkType = dynamic.networkType;
        this.networkState = dynamic.networkState;
        this.activeScreen = dynamic.activeScreen;
        this.orientation = dynamic.orientation;
    }

    private String sanitize(String message) {
        return message.replace("'", "''").replaceAll("\\\\", "\\\\\\\\");
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
        this.tag = sanitize(tag);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = sanitize(message);
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

    /**
     * Compact, readable one-liner with only the per-log (variable) information. The
     * constant device/app context is no longer repeated here; text appenders write it
     * once as a header via {@link LogFormatter#header(LOG)}.
     */
    @Override
    public String toString() {
        return LogFormatter.compactLine(this);
    }
}
