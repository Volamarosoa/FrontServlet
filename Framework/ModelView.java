package etu2068.modelView;
import java.util.HashMap;
public class ModelView {
    String view;
    HashMap<String, Object> data;

    
    public ModelView() {
        this.setData(new HashMap<String,Object>());
    }
    
    public ModelView(String view) {
        this.setView(view);
        this.setData(new HashMap<String,Object>());
    }
    
    public String getView() {
        return this.view;
    }
    
    public void setView(String view) {
        this.view = view;
    }
    
    public HashMap<String, Object> getData() {
        return this.data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public void addItem(String key, Object value) {
        this.getData().put(key, value);
    }
}