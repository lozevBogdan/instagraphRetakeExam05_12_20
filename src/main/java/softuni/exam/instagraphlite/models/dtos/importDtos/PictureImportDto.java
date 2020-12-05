package softuni.exam.instagraphlite.models.dtos.importDtos;

import com.google.gson.annotations.Expose;
import org.springframework.jmx.export.annotation.ManagedAttribute;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PictureImportDto {

    @Expose
    private String path;
    @Expose
    private double size;

    public PictureImportDto() {
    }
    @NotNull
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @NotNull
    @Min(value = 500)
    @Max(value = 60000)
    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
