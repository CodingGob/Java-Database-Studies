package entities;

import java.util.Objects;

public class Seller {
    Integer id;
    String name;
    String email;
    java.util.Date birthDate;
    double baseSalary;
    Integer departmentId;

    public Seller(String name, String email, java.util.Date birthDate, double baseSalary, Integer departmentId) {
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.baseSalary = baseSalary;
        this.departmentId = departmentId;
    }

    public Seller(Integer id, String name, String email, java.util.Date birthDate, double baseSalary, Integer departmentId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.baseSalary = baseSalary;
        this.departmentId = departmentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public java.util.Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(java.util.Date birthDate) {
        this.birthDate = birthDate;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Integer getDepartment() {
        return departmentId;
    }

    public void setDepartment(Integer departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "Seller [id=" + this.getId() 
                + ", name=" + this.getName() 
                + ", email=" + this.getEmail() 
                + ", birthDate=" + this.getBirthDate() 
                + ", baseSalary=" + this.getBaseSalary() 
                + ", departmentId=" + this.getDepartment() 
                + "]";
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Seller other = (Seller) obj;

        return Objects.equals(name, other.name) && Objects.equals(email, other.email);
    }
}