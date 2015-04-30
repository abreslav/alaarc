package net.alaarc.vm;

/**
 * Definition of a global variable in Alaarc program.
 *
 * @author dnpetrov
 */
public class VmGlobalVarDef {
    private final int id;
    private final String name;

    public VmGlobalVarDef(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "#" + id + ":" + name;
    }
}
