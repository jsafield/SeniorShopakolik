package com.shopakolik.seniorproject.model.shopakolikelements;

/**
 * Created by Yusuf on 07.05.2015.
 */
public class Credentials {
    private String acid, poolid, role;

    public String getAcid() {
        return acid;
    }

    public void setAcid(String acid) {
        this.acid = acid;
    }

    public String getPoolid() {
        return poolid;
    }

    public void setPoolid(String poolid) {
        this.poolid = poolid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Credentials(String acid, String poolid, String role) {
        this.acid = acid;
        this.poolid = poolid;
        this.role = role;
    }

}
