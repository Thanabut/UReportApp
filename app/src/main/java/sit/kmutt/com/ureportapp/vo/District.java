package sit.kmutt.com.ureportapp.vo;

/**
 * Created by Thanabut on 5/9/2557.
 */
public class District {
    private String id_district;
    private String name = "";
    private String province_id ;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getId_district() {
        return id_district;
    }

    public void setId_district(String id_district) {
        this.id_district = id_district;
    }
}
