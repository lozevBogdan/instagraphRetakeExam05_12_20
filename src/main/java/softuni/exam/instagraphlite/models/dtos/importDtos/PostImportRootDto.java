package softuni.exam.instagraphlite.models.dtos.importDtos;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement(name = "posts")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostImportRootDto {

    @XmlElement
    private List<PostImportDto> post;

    public PostImportRootDto() {
    }

    public List<PostImportDto> getPost() {
        return post;
    }

    public void setPost(List<PostImportDto> post) {
        this.post = post;
    }
}
