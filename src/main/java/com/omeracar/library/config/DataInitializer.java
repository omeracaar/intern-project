package com.omeracar.library.config;

import com.github.javafaker.Faker;
import com.omeracar.library.entity.Book;
import com.omeracar.library.entity.Library;
import com.omeracar.library.entity.Review;
import com.omeracar.library.repository.IBookRepository;
import com.omeracar.library.repository.ILibraryRepository;
import com.omeracar.library.repository.IReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final ILibraryRepository libraryRepository;
    private final IBookRepository bookRepository;
    private final IReviewRepository reviewRepository;

    @Override
    public void run(String... args) throws Exception {

        Faker faker = new Faker();
        Random random = new Random();

        //5 library
        List<Library> libraries = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Library library = new Library();
            library.setName(faker.company().name() + " Kütüphanesi");
            library.setLocation(faker.address().fullAddress());
            library.setPhoneNumber(faker.phoneNumber().cellPhone());
            libraries.add(library);
        }
        libraryRepository.saveAll(libraries);

        //400 book
        List<Book> books = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Library randomLibrary = libraries.get(random.nextInt(libraries.size()));

            Book book = new Book();
            book.setBookName(faker.book().title());
            book.setAuthor(faker.book().author());
            book.setPageCount(faker.number().numberBetween(100, 800));
            book.setLibrary(randomLibrary);

            books.add(book);
        }
        bookRepository.saveAll(books);

        // booklara rastgele review ekle
        List<Review> reviews = new ArrayList<>();
        for (Book book : books) {
            int reviewCount = random.nextInt(5) + 1; // her book a 1 ila 5 arası yorum

            for (int j = 0; j < reviewCount; j++) {
                Review review = Review.builder()
                        .content(faker.lorem().sentence(5)) // 5 kelimelik lorem ipsum review
                        .rating((double) faker.number().numberBetween(1, 6))
                        .book(book)
                        .build();
                reviews.add(review);
            }
        }
        reviewRepository.saveAll(reviews);
    }
}


