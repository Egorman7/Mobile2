package app.and.mobile2.models;

public class ListItemModel {
    private String url;
    private boolean approved;

    public ListItemModel(String url, boolean approved) {
        this.url = url;
        this.approved = approved;
    }

    public String getUrl() {
        return url;
    }

    public boolean isApproved() {
        return approved;
    }
}
