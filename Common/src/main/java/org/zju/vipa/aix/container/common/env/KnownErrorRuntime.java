package org.zju.vipa.aix.container.common.env;

import org.zju.vipa.aix.container.common.db.entity.aix.KnownError;

/**
 * @Date: 2021/1/20 10:44
 * @Author: EricMa
 * @Description: 可在线修改的错误模板
 */
//@Data
public class KnownErrorRuntime {
    private String name;
    private String key_words;
    private String repair_cmds;

    public KnownErrorRuntime(String name, String key_words, String repair_cmds) {
        this.name = name;
        this.key_words = key_words;
        this.repair_cmds = repair_cmds;
    }
    public KnownErrorRuntime(KnownError knownError) {
        this.name = knownError.getName();
        this.key_words = knownError.getKeyWords();
        this.repair_cmds = knownError.getRepairCmds();
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
