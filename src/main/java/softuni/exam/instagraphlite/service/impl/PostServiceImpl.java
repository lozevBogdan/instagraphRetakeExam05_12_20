package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.importDtos.PostImportDto;
import softuni.exam.instagraphlite.models.dtos.importDtos.PostImportRootDto;
import softuni.exam.instagraphlite.models.entites.Picture;
import softuni.exam.instagraphlite.models.entites.Post;
import softuni.exam.instagraphlite.models.entites.User;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.util.ValidatorUtil;
import softuni.exam.instagraphlite.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final String POST_PATH = "src/main/resources/files/posts.xml";

    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final XmlParser xmlParser;
    private final ValidatorUtil validatorUtil;
    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;

    public PostServiceImpl(ModelMapper modelMapper, PostRepository postRepository,
                           XmlParser xmlParser, ValidatorUtil validatorUtil,
                           PictureRepository pictureRepository, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
        this.xmlParser = xmlParser;
        this.validatorUtil = validatorUtil;
        this.pictureRepository = pictureRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Boolean аreImported() {

        return this.postRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(POST_PATH));
    }

    @Override
    public String importPosts() throws IOException, JAXBException {

        StringBuilder sb = new StringBuilder();


        PostImportRootDto postRootDto =
                this.xmlParser.parseXml(PostImportRootDto.class, POST_PATH);

        for (PostImportDto postDto : postRootDto.getPost()) {

            Optional<User> userByUsername =
                    this.userRepository.findByUsername(postDto.getUser().getUsername());

            Optional<Picture> pictureByPath =
                    this.pictureRepository.findByPath(postDto.getPicture().getPath());

            if(this.validatorUtil.isValid(postDto) && userByUsername.isPresent()
                    && pictureByPath.isPresent()){

                Post post = this.modelMapper.map(postDto, Post.class);

                post.setUser(userByUsername.get());
                post.setPicture(pictureByPath.get());

                this.postRepository.saveAndFlush(post);

                sb.append(String.format("Successfully imported Post, made by %s%n",
                        post.getUser().getUsername()));


            }else {
                sb.append("Invalid Post").append(System.lineSeparator());
            }


        }


        return sb.toString();
    }
}
