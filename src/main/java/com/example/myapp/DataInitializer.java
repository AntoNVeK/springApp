package com.example.myapp;

import com.example.myapp.model.*;
import com.example.myapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserFollowersRepository followersRepository;

    public DataInitializer(UserRepository userRepository,
                           UserProfileRepository profileRepository,
                           PostRepository postRepository,
                           CommentRepository commentRepository,
                           UserFollowersRepository followersRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.followersRepository = followersRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔍 Проверка базы данных...");

        if (userRepository.count() == 0) {
            System.out.println("📝 Добавляю тестовые данные пользователей...");

            // Создаём пользователей
            User ivan = userRepository.save(new User("Иван Петров", "ivan@example.com", 25));
            User maria = userRepository.save(new User("Мария Сидорова", "maria@example.com", 30));
            User alex = userRepository.save(new User("Алексей Иванов", "alex@example.com", 28));
            User olga = userRepository.save(new User("Ольга Николаева", "olga@example.com", 22));
            User sergey = userRepository.save(new User("Сергей Васильев", "sergey@example.com", 35));

            List<User> users = List.of(ivan, maria, alex, olga, sergey);

            System.out.println(" Добавлено " + users.size() + " пользователей");

            for (User user : users) {
                UserProfile profile = new UserProfile();
                profile.setUserId(user.getId());
                profile.setFullName(user.getUserName());
                profile.setBio("Привет, я " + user.getUserName());
                profile.setCity("Город " + user.getId());
                profile.setStreet("Улица " + user.getId());
                profile.setZipCode("1000" + user.getId());
                profileRepository.save(profile);
            }
            System.out.println(" Добавлены профили пользователей");

            // Создаём посты
            Post post1 = postRepository.save(new Post("Первый пост Ивана", "Содержимое поста 1", ivan.getId()));
            Post post2 = postRepository.save(new Post("Пост Марии", "Содержимое поста 2", maria.getId()));
            Post post3 = postRepository.save(new Post("Пост Алексея", "Содержимое поста 3", alex.getId()));

            System.out.println(" Добавлены посты");

            // Создаём комментарии
            commentRepository.save(new Comment("Отличный пост!", post1.getId(), maria.getId(), null));
            commentRepository.save(new Comment("Спасибо!", post1.getId(), ivan.getId(), 1L));
            commentRepository.save(new Comment("Интересно!", post2.getId(), olga.getId(), null));

            System.out.println(" Добавлены комментарии");

            // Создаём подписки (followers)
            followersRepository.follow(ivan.getId(), maria.getId());   // Мария подписана на Ивана
            followersRepository.follow(ivan.getId(), alex.getId());    // Алекс подписан на Ивана
            followersRepository.follow(maria.getId(), olga.getId());   // Ольга подписана на Марию
            followersRepository.follow(sergey.getId(), ivan.getId());  // Иван подписан на Сергея

            System.out.println(" Добавлены подписки");
        } else {
            System.out.println(" База уже содержит данные:");
            System.out.println("Пользователей: " + userRepository.count());
        }

        // Показываем всех пользователей
        System.out.println("\n👥 Список пользователей:");
        userRepository.findAll().forEach(user ->
                System.out.println("  " + user.getId() + ": " + user.getUserName() +
                        " (" + user.getEmail() + "), возраст: " + user.getAge())
        );
    }

}
