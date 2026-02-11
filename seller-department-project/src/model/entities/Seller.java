package model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

public class Seller implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private BigDecimal baseSalary;
    private Department department;

    public Seller() {}

    public Seller(Seller other) {
        this.id = other.getId();
        this.name = other.getName();
        this.email = other.getEmail();
        this.birthDate = other.getBirthDate();
        this.baseSalary = other.getBaseSalary();
        this.department = other.getDepartment();
    }

    public Seller(String name, String email, LocalDate birthDate, BigDecimal baseSalary, Department department) {
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.baseSalary = baseSalary;
        this.department = department;
    }

    public Seller(Integer id, String name, String email, LocalDate birthDate, BigDecimal baseSalary, Department department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.baseSalary = baseSalary;
        this.department = department;
    }

    
    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Department getDepartment() {
        return this.department;
    }
    
    public void setDepartment(Department department) {
        this.department = department;
    }


    @Override
    public String toString() {
        return 
            "[Id: " + this.getId() 
            + ", Name: " + this.getName() 
            + ", Email: " + this.getEmail()
            + ", BirthDate: " + this.getBirthDate()
            + ", BaseSalary: " + this.getBaseSalary().setScale(2, RoundingMode.HALF_UP)
            + ", Department: " + this.getDepartment()
            + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Seller other)) return false;
        return Objects.equals(this.id, other.id);
    }
}
