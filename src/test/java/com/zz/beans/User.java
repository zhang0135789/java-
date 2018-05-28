package java.com.zz.beans;

import java.io.Serializable;

/**
 * @Author: zz
 * @Description:
 * @Date: обнГ 4:07 2018/5/28 0028
 * @Modified By
 */
public class User implements Serializable {
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
