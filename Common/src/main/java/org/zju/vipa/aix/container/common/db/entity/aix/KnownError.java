package org.zju.vipa.aix.container.common.db.entity.aix;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Date: 2021/1/22 15:15
 * @Author: EricMa
 * @Description:
 */
public class KnownError  implements Serializable {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private String name;
    private String key_words;
    private String repair_cmds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey_words() {
        return key_words;
    }

    public void setKey_words(String key_words) {
        this.key_words = key_words;
    }

    public String getRepair_cmds() {
        return repair_cmds;
    }

    public void setRepair_cmds(String repair_cmds) {
        this.repair_cmds = repair_cmds;
    }
}
