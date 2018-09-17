package com.constantine.movierama.web;

import com.constantine.movierama.domain.Movie;
import com.constantine.movierama.domain.User;
import com.constantine.movierama.service.RestClientService;
import com.constantine.movierama.utils.Rating;
import com.constantine.movierama.utils.SortedBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("session")
public class MovieramaController {

    @Autowired
    private RestClientService restClientService;

    @Autowired
    private User user;

    @Autowired
    private SortedBy sortedBy;

    @Autowired
    private Integer moviesByUserId;

    private static final String REDIRECT = "redirect:/";

    @GetMapping(value = "/")
    public String index(Model model) {

        if (moviesByUserId > 0) {
            return "redirect:/user/" + moviesByUserId;
        }

        Map<String, Object> map = new HashMap<>();

        List<Movie> movies = restClientService.findAllMovies(sortedBy);

        sortMovies(movies);
        map.put("movies", movies);

        checkRatings(map);

        model.addAllAttributes(map);

        return "index";
    }

    @GetMapping(value = "/resetview")
    public String resetView(Model model) {
        moviesByUserId = 0;
        sortedBy = SortedBy.DATE;

        return REDIRECT;
    }

    @GetMapping(value = "/orderBy/{sorted}")
    public String orderBy(@ModelAttribute("sorted") String sorted, Model model) {
        this.sortedBy = SortedBy.valueOf(sorted);

        return REDIRECT;
    }

    @GetMapping("/login")
    public String login(Model model) {

        model.addAttribute("user", this.user);
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user) {
        this.user = restClientService.login(user);

        if (this.user == null) this.user = new User();

        return REDIRECT;
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        this.user = new User();
        return REDIRECT;
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String submit(@ModelAttribute User user) {
        this.user = restClientService.create(user);
        return REDIRECT;
    }

    @GetMapping("/create-movie")
    public String movieForm(Model model) {

        Movie movie = new Movie();
        model.addAttribute("movie", movie);
        return "movie";
    }

    @PostMapping("/create-movie")
    public String createMovie(@ModelAttribute Movie movie, ModelMap model) {

        restClientService.create(user.getId(), movie);

        return REDIRECT;
    }

    @GetMapping("/like/{id}")
    public String like(@ModelAttribute("id") Integer id, Model model) {

        Movie movie;

        if ((movie = restClientService.findMovieById(id)) != null) {

            user = restClientService.likeMovieForUser(user.getId(), movie.getId());
        }

        return REDIRECT;
    }

    @GetMapping("/hate/{id}")
    public String hate(@ModelAttribute("id") Integer id, Model model) {

        Movie movie;

        if ((movie = restClientService.findMovieById(id)) != null) {

            user = restClientService.hateMovieForUser(user.getId(), movie.getId());
        }

        return REDIRECT;
    }

    @GetMapping("/user/{id}")
    public String moviesByUser(@ModelAttribute("id") Integer id, Model model) {

        User user1;

        if ((user1 = restClientService.findUserById(id)) != null) {

            this.moviesByUserId = user1.getId();

            Map<String, Object> map = new HashMap<>();

            List<Movie> movies = restClientService.findMoviesByUser(moviesByUserId, sortedBy);

            map.put("movies", movies);

            checkRatings(map);

            model.addAllAttributes(map);

            return "index";
        }

        return REDIRECT;
    }

    private void sortMovies(List<Movie> movies) {
        movies.sort((o1, o2) -> {
            switch (sortedBy) {
                case DATE:
                    return o2.getDateOfPublication().compareTo(o1.getDateOfPublication());
                case HATES:
                    return Long.compare(o2.getHates(), o1.getHates());
                case LIKES:
                    return Long.compare(o2.getLikes(), o1.getLikes());
                default:
                    return 0;
            }
        });
    }

    private void checkRatings(Map<String, Object> map) {
        if (this.user.getId() != 0) {
            map.put("user", this.user);

            Map<Integer, Rating> ratings = restClientService.ratingsForUserWithId(this.user.getId());

            map.put("ratings", ratings);
        }
    }

    /**
     * Another way to overpass the issues with map.get function in thymeleaf
     * @param ratings
     * @param key
     * @return
     */
    public static String getValueFromMap(Map<Integer, String> ratings, int key) {

        return ratings.get(key);
    }

    /**
     * A static method to be used in the front end for picking the suitable message according the time the user
     * posted the movie
     * @param dateTime
     * @return
     */
    public static String[] findDays(LocalDateTime dateTime) {

        Duration duration = Duration.between(dateTime, LocalDateTime.now(ZoneId.of("UTC")));

        String type = "MINUTES";

        if (duration.toHours() > 24) {
            type = "DAYS";
            return new String[]{String.valueOf(duration.toDays()), type};
        } else if (duration.toMinutes() > 59) {
            type = "HOURS";
            return new String[]{String.valueOf(duration.toHours()), type};
        }

        return new String[]{String.valueOf(duration.toMinutes()), type};
    }
}