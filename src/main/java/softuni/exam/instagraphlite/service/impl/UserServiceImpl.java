package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.importDtos.UserImportDto;
import softuni.exam.instagraphlite.models.entites.Picture;
import softuni.exam.instagraphlite.models.entites.Post;
import softuni.exam.instagraphlite.models.entites.User;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidatorUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final String USERS_PATH = "src/main/resources/files/users.json";

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ValidatorUtil validatorUtil;
    private final Gson gson;
    private final PictureRepository pictureRepository;

    @Autowired
    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository,
                           ValidatorUtil validatorUtil, Gson gson, PictureRepository pictureRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.validatorUtil = validatorUtil;
        this.gson = gson;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public Boolean аreImported() {

        return this.userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(USERS_PATH));
    }

    @Override
    public String importUsers() throws IOException {
        StringBuilder sb = new StringBuilder();


        UserImportDto[] userImportDtos =
                this.gson.fromJson(this.readFromFileContent(), UserImportDto[].class);

        for (UserImportDto userDto : userImportDtos) {

            Optional<Picture> pictureByPath =
                    this.pictureRepository.findByPath(userDto.getProfilePicture());

            Optional<User> userByUsername =
                    this.userRepository.findByUsername(userDto.getUsername());

            if (this.validatorUtil.isValid(userDto) && pictureByPath.isPresent()
                    && userByUsername.isEmpty()){

                User user = this.modelMapper.map(userDto, User.class);

                user.setProfilePicture(pictureByPath.get());

                this.userRepository.saveAndFlush(user);

                sb.append(String.format("Successfully imported User: %s%n",
                        user.getUsername()));


            }else {
                sb.append("Invalid User").append(System.lineSeparator());
            }


        }


        return sb.toString();
    }

    @Override
    public String exportUsersWithTheirPosts() {

        StringBuilder sb = new StringBuilder();

        List<User> usersWithTheirPosts = this.userRepository.getUsersWithTheirPosts();


        for (User user : usersWithTheirPosts) {

            List<Post> posts = user.getPosts();

            posts.sort(Comparator.comparingDouble(p -> p.getPicture().getSize()));

            sb.append(String.format("User: %s\n" +
                    "Post count: %d\n",
                    user.getUsername(),user.getPosts().size()));

            for (Post post : posts) {

                sb.append(String.format("==Post Details:\n" +
                        "----Caption: %s\n" +
                        "----Picture Size: %.2f\n",
                        post.getCaption(),post.getPicture().getSize()));
            }


        }
        sb.append(System.lineSeparator());


        return sb.toString();
    }
}
