package warsztaty.spring.ailleron.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@TableGenerator(name = "test", initialValue = 3)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "test")
    private Long id;
    @Size(min = 2, max = 20, message = "{name.message}")
    private String name;
    private String surname;
    @Min(value = 18, message = "Uzytkownik zakładający konto powinien być pełnoletni")
    @Max(value = 100, message = "Nie wierzę, że masz ponad 100 lat")
    private Integer age;

    public User() {
    }

    public User(Long id, String name, String surname, Integer age) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
