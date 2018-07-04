package nowrek.newsfilter.DataStructures;

public class ChangeConfig<T extends ConfigData> {

    public enum ChangeType{
        Modified,
        Added,
        Removed
    }

    private final ChangeType type;
    private final T data;

    public ChangeConfig(ChangeType type, T data){
        this.type = type;
        this.data = data;
    }

    public ChangeType getChangeType(){
        return type;
    }

    public Class getDataType(){
        return data.getClass();
    }

    public T getChangeData(){
        return data;
    }
}
