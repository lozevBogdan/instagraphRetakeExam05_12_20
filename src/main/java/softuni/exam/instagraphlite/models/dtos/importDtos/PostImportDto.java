package softuni.exam.instagraphlite.models.dtos.importDtos;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "post")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostImportDto {

    @XmlElement
    private String caption;
    @XmlElement
    private UserImportPostDto user;
    @XmlElement
    private PictureImportPostDto picture;

    public PostImportDto() {
    }


    @NotNull
    @Length(min = 21)
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public UserImportPostDto getUser() {
        return user;
    }

    public void setUser(UserImportPostDto user) {
        this.user = user;
    }

    public PictureImportPostDto getPicture() {
        return picture;
    }

    public void setPicture(PictureImportPostDto picture) {
        this.picture = picture;
    }
}
