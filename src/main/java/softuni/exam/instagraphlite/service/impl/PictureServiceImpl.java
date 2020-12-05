package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.hibernate.validator.internal.engine.ValidatorImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.importDtos.PictureImportDto;
import softuni.exam.instagraphlite.models.entites.Picture;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.util.ValidatorUtil;
import softuni.exam.instagraphlite.util.ValidatorUtilImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class PictureServiceImpl implements PictureService {

    private final String PICTURE_PATH = "src/main/resources/files/pictures.json";

    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidatorUtil validatorUtil;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository,
                              ModelMapper modelMapper, Gson gson, ValidatorUtil validatorUtil) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public Boolean areImported() {

        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(PICTURE_PATH));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder sb  = new StringBuilder();

        PictureImportDto[] pictureImportDtos =
                this.gson.fromJson(this.readFromFileContent(), PictureImportDto[].class);


        for (PictureImportDto pictureDto : pictureImportDtos) {

            Optional<Picture> pictureByPath =
                    this.pictureRepository.findByPath(pictureDto.getPath());


            if (this.validatorUtil.isValid(pictureDto) && pictureByPath.isEmpty()){

                Picture picture = this.modelMapper.map(pictureDto, Picture.class);

                this.pictureRepository.saveAndFlush(picture);

                sb.append(String.format("Successfully imported Picture, with size %.2f%n",
                        picture.getSize()));

            }else {

                sb.append("Invalid Picture").append(System.lineSeparator());
            }

        }



        return sb.toString();
    }

    @Override
    public String exportPictures() {
        StringBuilder sb = new StringBuilder();

        List<Picture> pictures = this.pictureRepository.exportPicturesWuthSizeBiggerThan30000();

        for (Picture picture : pictures) {

            sb.append(String.format("%.2f – %s%n",
                    picture.getSize(),picture.getPath()));

        }


        return sb.toString();
    }
}
