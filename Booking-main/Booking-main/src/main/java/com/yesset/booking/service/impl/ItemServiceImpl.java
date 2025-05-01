package com.yesset.booking.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesset.booking.dto.item.*;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.*;
import com.yesset.booking.mapper.item.ItemMapper;
import com.yesset.booking.repositiry.*;
import com.yesset.booking.service.ItemService;
import com.yesset.booking.specification.item.ItemSpecification;
import com.yesset.booking.util.BookStatus;
import com.yesset.booking.util.UserApp;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.yesset.booking.util.BookStatus.PENDING;
import static com.yesset.booking.util.ErrorStatus.*;
import static com.yesset.booking.util.ItemStatus.ACTIVE;
import static com.yesset.booking.util.ItemStatus.INACTIVE;
import static com.yesset.booking.util.PayForView.COMPLETED;
import static com.yesset.booking.util.PayForView.INCOMPLETE;
import static com.yesset.booking.util.RoleEnum.ROLE_USER;

@Service
@Transactional
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final FileRepository fileRepository;
    private final FileServiceImpl fileServiceImpl;
    private final CategoryServiceImpl categoryServiceImpl;
    private final CategoryRepository categoryRepository;
    private final MicroDistinctRepository microDistinctRepository;
    private final LocationRepository locationRepository;
    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final PayForViewRepository payForViewRepository;
    private final RentalRepository rentalRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public ItemServiceImpl(ItemRepository itemRepository, FileRepository fileRepository, FileServiceImpl fileServiceImpl, CategoryServiceImpl categoryServiceImpl, CategoryRepository categoryRepository, MicroDistinctRepository microDistinctRepository, LocationRepository locationRepository, VerificationRepository verificationRepository, UserRepository userRepository, PayForViewRepository payForViewRepository, RentalRepository rentalRepository) {
        this.itemRepository = itemRepository;
        this.fileRepository = fileRepository;
        this.fileServiceImpl = fileServiceImpl;
        this.categoryServiceImpl = categoryServiceImpl;
        this.categoryRepository = categoryRepository;
        this.microDistinctRepository = microDistinctRepository;
        this.locationRepository = locationRepository;
        this.verificationRepository = verificationRepository;
        this.userRepository = userRepository;
        this.payForViewRepository = payForViewRepository;
        this.rentalRepository = rentalRepository;
    }
    private final ItemRepository itemRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseAnswer<Page<ItemPreViewDto>> getFilteredItems(ItemRequestDto request) {
        ResponseAnswer<Page<ItemPreViewDto>> answer = new ResponseAnswer<>();
        try {
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
            Specification<Item> spec = ItemSpecification.filterItems(request, "available");
            Page<Item> itemPage = itemRepository.findAll(spec, pageable);
            answer.setSuccess(Boolean.TRUE);
            answer.setBody(itemPage.map(ItemMapper::toDto));
        }catch (Exception e){
            answer.setSuccess(Boolean.FALSE);
            answer.setStatus(PULL_DATA_ERROR.toString());
        }
        return answer;
    }

    @Override
    public ResponseAnswer<ItemFullViewDto> getFullData(Long itemId) {
        ResponseAnswer<ItemFullViewDto> response = new ResponseAnswer<>();
        response.setSuccess(true);
        try {
            Optional<Item> itemOptional = itemRepository.findById(itemId);
            if (itemOptional.isEmpty())
                throw new Exception("Item not found");
            Item item = itemOptional.get();
            List<File> files = fileServiceImpl.getAllFilesByItem(itemId);
            ItemFullViewDto itemFullViewDto = ItemFullViewDto.builder()
                    .name(item.getName())
                    .description(item.getDescription())
                    .categoryMap(categoryMap(item.getCategoryId()))
                    .pricePerDay(item.getPricePerDay())
                    .attributes(getAttributesAsMap(item.getAttributes()))
                    .available(item.getAvailable())
                    .files(files)
                    .rate(calculateRate(itemId))
                    .user(getOwnerData(item.getOwnerId()))
                    .build();
            response.setBody(itemFullViewDto);
        }catch (Exception e){
            response.setSuccess(Boolean.FALSE);
            response.setStatus(PULL_DATA_ERROR.toString());
        }
        return response;
    }

    public Map<String, String> getAttributesAsMap(String attributes) {
        try {
            return objectMapper.readValue(attributes, new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Ошибка парсинга JSON", e);
        }
    }

    private User getOwnerData(Long ownerId) {
        return userRepository.findById(ownerId).orElse(null);
    }

    @Override
    @Transactional
    public ResponseAnswer<Object> createItemByUser(ItemAddDto request) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(true);
        try {
            User user = UserApp.getCurrentUser();
            if (user == null) throw new Exception("User not found");
            Item item = createitem(request,user);
            Verification verification = createVerificationDocument(item, request.getVerificationDocument());
            if (verification == null) throw new Exception("Verification not found");
            Optional<MicroDistinct> microDistinct = microDistinctRepository.findById(request.getMicroDistinctId());
            if (microDistinct.isEmpty()) throw new Exception("MicroDistinct not found");
            createLocation(microDistinct.get(), item, request.getFullAddress());
            createFiles(request.getUrls(), item);
        }catch (Exception e){
            response.setSuccess(Boolean.FALSE);
            response.setStatus(ITEM_CREATION_ERROR.toString());
        }
        return response;
    }

    private Verification createVerificationDocument(Item item, @NotNull(message = "verificationDocument обязательна") @NotBlank(message = "verificationDocument не должна быть пустой") String verificationDocument) {
        try {
            return verificationRepository.save(Verification.builder()
                    .fileUrl(verificationDocument)
                    .item(item)
                    .build());
        }catch (Exception e){
            return null;
        }

    }

    private Location createLocation(MicroDistinct microDistinct, Item item, @NotNull(message = "Полный адрес обязательна") @NotBlank(message = "Полный адрес не должна быть пустой") String fullAddress) {
        try {
            Location location = new Location();
            location.setItem(item);
            location.setMicrodistrict(microDistinct);
            location.setStreet(fullAddress);
            return locationRepository.save(location);
        }catch (Exception e){
            log.error("Ошибка: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public ResponseAnswer<Object> delete(Long id) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);
        try {
            Item item = itemRepository.findById(id).get();
            item.setDeleted(Boolean.TRUE);
            itemRepository.save(item);
        }catch (Exception e){
            response.setSuccess(Boolean.FALSE);
            response.setStatus(ITEM_DELETION_ERROR.toString());
        }
        return response;
    }

    @Override
    public ResponseAnswer<Page<ItemPreViewDto>> getCheckInItems(ItemRequestDto request) {
        ResponseAnswer<Page<ItemPreViewDto>> answer = new ResponseAnswer<>();
        answer.setSuccess(Boolean.TRUE);
        try {
            User user = UserApp.getCurrentUser();
            if (user == null) throw new Exception("User not found");
            if (isHimItemOwner(user)) {
                request.setOwnerId(user.getId());
            }
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
            Specification<Item> spec = ItemSpecification.filterItems(request, "checkIn");
            Page<Item> itemPage = itemRepository.findAll(spec, pageable);
            answer.setBody(itemPage.map(ItemMapper::toDto));
        }catch (Exception e){
            answer.setSuccess(Boolean.FALSE);
            answer.setStatus(ITEM_CHECKIN_ERROR.toString());
        }
        return answer;
    }

    @Override
    public ResponseAnswer<Page<ItemPreViewDto>> getRejectedItems(ItemRequestDto request) {
        ResponseAnswer<Page<ItemPreViewDto>> answer = new ResponseAnswer<>();
        try {
            User user = UserApp.getCurrentUser();
            if (user == null) throw new Exception("User not found");
            if (isHimItemOwner(user))
                request.setOwnerId(user.getId());
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
            Specification<Item> spec = ItemSpecification.filterItems(request, "rejected");
            Page<Item> itemPage = itemRepository.findAll(spec, pageable);
            answer.setSuccess(Boolean.TRUE);
            answer.setBody(itemPage.map(ItemMapper::toDto));
        }catch (Exception e){
            answer.setSuccess(Boolean.FALSE);
            answer.setStatus(PULL_DATA_ERROR.toString());
        }
        return answer;
    }

    private boolean isHimItemOwner(User user) {
        try {
            return user.getRole().getName().equals(ROLE_USER.toString());
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public ResponseAnswer<Page<ItemPreViewDto>> getApprovedItems(ItemRequestDto request) {
        ResponseAnswer<Page<ItemPreViewDto>> answer = new ResponseAnswer<>();
        try {
            User user = UserApp.getCurrentUser();
            if (user == null) throw new Exception("User not found");
            if (isHimItemOwner(user))
                request.setOwnerId(user.getId());
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
            Specification<Item> spec = ItemSpecification.filterItems(request, "approved");
            Page<Item> itemPage = itemRepository.findAll(spec, pageable);
            answer.setSuccess(Boolean.TRUE);
            answer.setBody(itemPage.map(ItemMapper::toDto));
        }catch (Exception e){
            answer.setSuccess(Boolean.FALSE);
            answer.setStatus(PULL_DATA_ERROR.toString());

        }
        return answer;
    }

    @Override
    public ResponseAnswer<Object> reject(Long id) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);
        try {
            Item item = itemRepository.findById(id)
                    .orElseThrow(() -> new Exception("Item not found"));
            item.setRejected(Boolean.TRUE);
            itemRepository.save(item);
        }catch (Exception e){
            response.setSuccess(Boolean.FALSE);
            response.setStatus(ITEM_REJECT_ERROR.toString());
        }
        return response;
    }

    @Override
    public ResponseAnswer<Object> approve(Long id) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);
        try {
            Item item = itemRepository.findById(id).get();
            if (item == null) throw new RuntimeException("Item not found");
            item.setApproved(Boolean.TRUE);
            itemRepository.save(item);
        }catch (Exception e){
            response.setSuccess(Boolean.FALSE);
            response.setStatus(ITEM_APPROVE_ERROR.toString());
        }
        return response;
    }

    @Override
    public ResponseAnswer<ItemAdminFullViewDto> getFullDataByAdmin(Long itemId) {
        ResponseAnswer<ItemAdminFullViewDto> response = new ResponseAnswer<>();
        response.setSuccess(true);
        try {
            Optional<Item> itemOptional = itemRepository.findById(itemId);
            if (itemOptional.isEmpty())
                throw new Exception("Item not found");
            Item item = itemOptional.get();
            List<File> files = fileServiceImpl.getAllFilesByItem(itemId);
            Verification verification = verificationRepository.findByItem(item)
                    .orElseThrow(() -> new Exception("Verification not found"));
            ItemAdminFullViewDto itemAdminFullViewDto = ItemAdminFullViewDto.builder()
                    .name(item.getName())
                    .description(item.getDescription())
                    .categoryMap(categoryMap(item.getCategoryId()))
                    .pricePerDay(item.getPricePerDay())
                    .attributes(item.getAttributes())
                    .available(item.getAvailable())
                    .files(files)
                    .rate(calculateRate(itemId))
                    .verificationDocument(verification.getFileUrl())
                    .build();
            response.setBody(itemAdminFullViewDto);
        }catch (Exception e){
            response.setSuccess(Boolean.FALSE);
            response.setStatus(PULL_DATA_ERROR.toString());
        }
        return response;
    }

    @Override
    public ResponseAnswer<Object> payForView(ItemViewPayDto request) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);
        response.setStatus(COMPLETED.toString());
        Optional<PayForView> findExistingPay = Optional.empty();
        try {
            Item item = itemRepository.findById(request.getId())
                    .orElseThrow(() -> new Exception(SOURCE_NOT_FOUND_ERROR.toString()));
            int addedDays = request.getDays();
            Date startDate = convertStartDate(request.getFrom());
            findExistingPay = intersectInTable(item, startDate, addedDays);
            if (findExistingPay.isPresent()) throw new Exception(RENTAL_INTERSECTION_ERROR.toString());
            PayForView payForView = createPayForView(item, startDate, addTime(startDate, addedDays - 1));
            /*
            TODO ЛОГИКА ОПЛАТЫ
             */
            payForViewRepository.save(payForView);
        }catch (Exception e){
            String intersection = formatDateToString(findExistingPay.get().getViewFrom()) + " - " +
                    formatDateToString(findExistingPay.get().getViewTill());
            response.setSuccess(Boolean.FALSE);
            response.setBody(intersection);
            response.setStatus(e.getMessage());
            response.setReason(
                    "Таңдаған уақытыңыз осы тауар үшін алдын ала төленген " + intersection +
                            " аралығымен сәйкес келеді. Өтінеміз, басқа уақыт аралығын таңдаңыз."
            );
            response.setReasonRu(
                    "Выбранный вами период пересекается с уже ранее оплаченным периодом " + intersection +
                            ". Пожалуйста, выберите другой временной промежуток."

            );
        }
        return response;
    }

    public String formatDateToString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }




    @Override
    public ResponseAnswer<Object> cancelPaymentForView(Long paymentId) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);
        try {
            PayForView payForView = payForViewRepository.findById(paymentId)
                    .orElseThrow(() -> new Exception(SOURCE_NOT_FOUND_ERROR.toString()));
            if (itIsLateCancel(payForView.getViewFrom()))
                throw new Exception(RENTAL_LATE_CANCEL_ERROR.toString());
            payForView.setStatus(INACTIVE.toString());
            /*
            TODO ЛОГИКА ОТМЕНА ОПЛАТЫ
             */
            payForViewRepository.save(payForView);
        }catch (Exception e){
            response.setSuccess(Boolean.FALSE);
            response.setBody(e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseAnswer<Page<PayForView>> historyPaymentForView(long itemId, int page, int size) {
        ResponseAnswer<Page<PayForView>> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PayForView> payHistory = payForViewRepository.findAllByItemId(itemId, pageable);
            response.setBody(payHistory);
        } catch (Exception e) {
            response.setSuccess(Boolean.FALSE);
            response.setStatus(PULL_DATA_ERROR.toString());
        }
        return response;
    }

    @Override
    public ResponseAnswer<BookFullDto> getFullDataToBook(Long itemId) {
        ResponseAnswer<BookFullDto> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);
        try {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new Exception("Item not found"));
            BookFullDto bookFullDto = BookFullDto.builder()
                    .item(item)
                    .freeDayList(freeDayList(item))
                    .build();
            response.setBody(bookFullDto);
        } catch (Exception e) {
            response.setSuccess(Boolean.FALSE);
            response.setStatus(PULL_DATA_ERROR.toString());
        }
        return response;
    }

    @Override
    public ResponseAnswer<Object> bookNow(Long itemId, BookRequestDto request) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);
        try {
            User user = UserApp.getCurrentUser();
            if (user == null) throw new Exception();
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new Exception("Item not found"));
            User owner = userRepository.findById(item.getOwnerId())
                    .orElseThrow(() -> new Exception("Owner not found"));
            Rental rental = Rental.builder()
                    .item(item)
                    .startDatetime(convertStartDate(request.getFrom()))
                    .endDatetime(convertStartDate(request.getTo()))
                    .renter(user)
                    .owner(owner)
                    .totalPrice(BigDecimal.valueOf(request.getTotalPrice()))
                    .createdAt(LocalDateTime.now())
                    .status(PENDING.toString())
                    .build();
            rentalRepository.save(rental);
        } catch (Exception e) {
            response.setSuccess(Boolean.FALSE);
            response.setStatus(BOOKING_ERROR.toString());
        }
        return response;
    }

    @Override
    public ResponseAnswer<Object> activeBook(Long id) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);
        try {
            Rental rental = rentalRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Rental not found"));
            if (intersectInTableToBook(rental))
                throw new Exception("");
            rental.setStatus(BookStatus.APPROVED.toString());
        }catch (Exception e){
            response.setSuccess(Boolean.FALSE);
            response.setStatus(ACTIVE_BOOKING_ERROR.toString());
        }
        return response;
    }

    @Override
    public ResponseAnswer<Page<Rental>> historyForBookingOwner(Long itemId, int page, int size) {
        ResponseAnswer<Page<Rental>> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);
        try {
            User user = UserApp.getCurrentUser();
            if (user == null) throw new Exception(SOURCE_NOT_FOUND_ERROR.toString());
            Pageable pageable = PageRequest.of(page, size);
            response.setBody(rentalRepository.findByItemIdAndOwner(itemId,user.getId(), pageable));
        }catch (Exception e){
            response.setSuccess(Boolean.FALSE);
            response.setStatus(PULL_DATA_ERROR.toString());
        }
        return response;
    }

    @Override
    public ResponseAnswer<Page<Rental>> historyForBookingUser(Long itemId, int page, int size) {
        ResponseAnswer<Page<Rental>> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);
        try {
            User user = UserApp.getCurrentUser();
            if (user == null) throw new Exception(SOURCE_NOT_FOUND_ERROR.toString());
            Pageable pageable = PageRequest.of(page, size);
            response.setBody(rentalRepository.findByItemIdAndUser(itemId,user.getId(), pageable));
        }catch (Exception e){
            response.setSuccess(Boolean.FALSE);
            response.setStatus(PULL_DATA_ERROR.toString());
        }
        return response;
    }

    @Override
    public ResponseAnswer<Object> cancelBook(Long id) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);
        try {
            Rental rental = rentalRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException(SOURCE_NOT_FOUND_ERROR.toString()));
            if (itIsLateCancel(rental.getStartDatetime()))
                throw new Exception(LATE_CANCEL_ERROR.toString());
            rental.setStatus(BookStatus.CANCELED.toString());
        }catch (Exception e){
            response.setSuccess(Boolean.FALSE);
            response.setStatus(e.getMessage());
        }
        return response;
    }

    private boolean itIsLateCancel(Date current) {
        Date now = dateSetTime(new Date());
        current = dateSetTime(current);
        return current.before(now);
    }

    private boolean intersectInTableToBook(Rental rental) {
        try {
            Date from = dateSetTime(rental.getStartDatetime());
            Date to = dateSetTime(rental.getEndDatetime());
            LocalDate fromLocalDate = from.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate toLocalDate = to.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            Optional<Rental> rentalOptionalFrom = rentalRepository.findByItemIdAndDate(
                    rental.getItem().getId(),
                    fromLocalDate
            );
            Optional<Rental> optionalTo = rentalRepository.findByItemIdAndDate(
                    rental.getItem().getId(),
                    toLocalDate
            );
            Optional<Rental> finalResearch = rentalRepository.searchForExistingBook(
                    rental.getItem().getId(),
                    fromLocalDate,
                    toLocalDate
            );
            return rentalOptionalFrom.isPresent() || optionalTo.isPresent() || finalResearch.isPresent();
        }catch (Exception e){
            return true;
        }
    }

    private List<FreeDay> freeDayList(Item item) throws Exception {
        List<FreeDay> freeDays = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_MONTH, i);
                Date date = calendar.getTime();
                LocalDate localDate = date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                Optional<Rental> optionalRental = rentalRepository.findByItemIdAndDate(
                        item.getId(),
                        localDate);

                FreeDay freeDay = FreeDay.builder()
                        .month(localDate.getMonth().name())
                        .dayOfMonth(String.valueOf(localDate.getDayOfMonth()))
                        .dayOfWeek(localDate.getDayOfWeek().name())
                        .isFree(optionalRental.isEmpty())
                        .format(formatDateToString(date))
                        .build();
                freeDays.add(freeDay);
            }catch (Exception e){
                throw new Exception();
            }
        }
        return freeDays;
    }


    private boolean isThisLateCancel(Date payForView) {
        return !(new Date().before(payForView));
    }

    private PayForView createPayForView(Item item, Date viewFrom, Date viewTill) {
        return PayForView
                .builder()
                .item(item)
                .viewFrom(viewFrom)
                .viewTill(viewTill)
                .status(ACTIVE.toString())
                .build();
    }

    private Optional<PayForView> intersectInTable(Item item, Date startDate,
                                     @NotNull(message = "Поле 'days' не может быть null") @Min(value = 1, message = "Минимальное количество дней — 1") Integer days) {
        for (int i = 0; i < days; i++) {
            Date currentDate = addTime(startDate, i);
            try {
                Optional<PayForView> payForView = payForViewRepository.findIntersection(currentDate, item.getId());
                if (payForView.isPresent()) {
                    return payForView;
                }
            }catch (Exception e){
                log.error(e.getMessage());
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
    private Date addTime(Date current, int addedDays){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(Calendar.DAY_OF_MONTH, addedDays);
        return calendar.getTime();
    }

    private Date convertStartDate(@NotBlank(message = "Поле 'from' не может быть пустым") String from) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(from);
            date = dateSetTime(date);
            return date;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Неверный формат даты. Ожидается yyyy-MM-dd");
        }
    }

    public Date dateSetTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    private Item createitem(ItemAddDto request, User user) {
        try {
            Item item = new Item();
            item.setOwnerId(user.getId());
            item.setName(request.getName());
            item.setDescription(request.getDescription());
            item.setAttributes(request.getAttributes());
            item.setCategoryId(request.getCategoryId());
            item.setPricePerDay(BigDecimal.valueOf(request.getPricePerDay()));
            item.setAvailable(request.getAvailable());
            item.setOwnerId(user.getId());
            item.setDeleted(Boolean.FALSE);
            item.setApproved(Boolean.FALSE);
            item.setRejected(Boolean.FALSE);
            item.setStatus(INACTIVE.toString());
            return itemRepository.save(item);
        }catch (Exception e){
            return null;
        }
    }

    private void createFiles(@NotNull(message = "Список ссылок обязателен") @NotEmpty(message = "Необходимо добавить хотя бы одну ссылку") List<@NotBlank(message = "Ссылка не должна быть пустой") String> urls, Item item) {
        for (String url : urls) {
            File file = new File();
            file.setFileType("image");
            file.setItem(item);
            file.setFileUrl(url);
            file.setUploadedAt(LocalDateTime.now());
            fileRepository.save(file);
        }
    }

    private Double calculateRate(Long itemId) {
        return 0.0;
    }

    private String categoryMap(Long categoryId) {
        StringBuilder builder = new StringBuilder();
        Category category =  categoryRepository.findById(categoryId).get();
        if (category != null) {
        }
        Stack<String> stack = new Stack<>();
        while (category != null) {
            stack.push(category.getName());
            if (category.getParentCategoryId() == null) break;
            category = categoryRepository.findById(category.getParentCategoryId()).get();
        }
        while (!stack.isEmpty()){
            builder.append(stack.pop());
            if (stack.isEmpty()) break;
            builder.append(" -> ");
        }
        return builder.toString();
    }
}
