package restaurant.ms.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import restaurant.ms.core.dto.requests.CategoryCreateRq;
import restaurant.ms.core.dto.requests.CategoryUpdateRq;
import restaurant.ms.core.dto.responses.PageRs;
import restaurant.ms.core.dto.responses.CategorySearchRs;
import restaurant.ms.core.entities.Category;
import restaurant.ms.core.entities.RestaurantUser;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.CategoryRepo;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;


    public PageRs searchCategory(RestaurantUser restaurantUser,Integer page, Integer size, Locale locale) {
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Page<Category> categoryPage = categoryRepo.findAllBy(restaurantUser.getRestaurant(),pageable);

        List<Category> categoryList = categoryPage.getContent();

        if (categoryList == null) {
            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "no_data_found", locale);
        }

        List<CategorySearchRs> categorySearchRsList = categoryList.stream()
                .map(category -> category.toCategorySearchRs())
                .collect(Collectors.toList());

        return new PageRs(categoryPage.getTotalElements(), categoryPage.getTotalPages(), categorySearchRsList);
    }

    public void createCategory(CategoryCreateRq categoryCreateRq, RestaurantUser restaurantUser, Locale locale) {

        Category category = new Category();
        category.setCode(categoryCreateRq.getCode());
        category.setNameAr(categoryCreateRq.getNameAr());
        category.setNameEn(categoryCreateRq.getNameEn());
        category.setAvatar(categoryCreateRq.getAvatar());
        category.setRestaurant(restaurantUser.getRestaurant());
        category.setStatus(Status.ACTIVE);

        categoryRepo.save(category);
    }

    public void updateCategory(CategoryUpdateRq categoryUpdateRq, RestaurantUser restaurantUser, Locale locale) {

        Category category = new Category();
        category.setId(categoryUpdateRq.getCategoryId());
        category.setCode(categoryUpdateRq.getCode());
        category.setNameAr(categoryUpdateRq.getNameAr());
        category.setNameEn(categoryUpdateRq.getNameEn());
        category.setAvatar(categoryUpdateRq.getAvatar());
        category.setRestaurant(restaurantUser.getRestaurant());
        category.setStatus(Status.ACTIVE);

        categoryRepo.save(category);
    }


    public void activeOrInactiveCategory(Long categoryId, RestaurantUser restaurantUser, Locale locale) {

        Category category = categoryRepo.findCategoryById(categoryId);

        if(category == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(category.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(category.getStatus().equals(Status.INACTIVE)){
            category.setStatus(Status.ACTIVE);
        }else{
            if(category.getStatus().equals(Status.ACTIVE)){
                category.setStatus(Status.INACTIVE);
            }
        }

        categoryRepo.save(category);
    }

    public void deleteCategory(Long categoryId, RestaurantUser restaurantUser, Locale locale) {

        Category category = categoryRepo.findCategoryById(categoryId);

        if(category == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(category.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        category.setStatus(Status.DELETED);

        categoryRepo.save(category);
    }

}
