package org.zju.vipa.aix.container.common.db.entity.aix;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

public class KnownError implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private int id;
    private String name;
    private String keyWords;
    private String repairCmds;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }


    public String getRepairCmds() {
        return repairCmds;
    }

    public void setRepairCmds(String repairCmds) {
        this.repairCmds = repairCmds;
    }

}
