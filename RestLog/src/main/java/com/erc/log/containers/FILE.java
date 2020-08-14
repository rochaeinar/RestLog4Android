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
public class FILE extends Entity {

    @PrimaryKey
    @Field
    public long id;

    @Field
    public long date;

    @Field
    public String fullPath;

}
