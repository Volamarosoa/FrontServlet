package etu2068.modelView;
public class ModelView {
    String view;

    public ModelView() {}

    public ModelView(String view) {
        this.setView(view);
    }

    public String getView() {
        return this.view;
    }

    public void setView(String view) {
        this.view = view;
    }

}