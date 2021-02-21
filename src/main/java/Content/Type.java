package Content;

public class Type {

    BaseType baseType;
    SubType subType;

    public BaseType getBaseType() {
        return this.baseType;
    }

    public SubType getSubType() {
        return this.subType;
    }

    public Type(BaseType baseType, SubType subType) {
        this.baseType = baseType;
        this.subType = subType;
    }

    public Type(SubType subType) {
        this.subType = subType;
        if(subType == SubType.basicturret || subType == SubType.flakturret || subType == SubType.artilleryturret) {
            this.baseType = BaseType.turrettype;
        }
    }

    public static enum BaseType {
        turrettype
    }

    public static enum SubType {
        basicturret, flakturret, artilleryturret
    }
}
