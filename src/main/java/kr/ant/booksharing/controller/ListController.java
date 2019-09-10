package kr.ant.booksharing.controller;

import kr.ant.booksharing.model.Book.BookReq;
import kr.ant.booksharing.model.Book.BookRes;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.Transaction.TransactionReq;
import kr.ant.booksharing.service.ListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("lists")
public class ListController {

    private final ListService listService;

    public ListController(final ListService listService) {
        this.listService = listService;
    }

    /**
     * 모든 판매 도서 목록 조회
     *
     * @return ResponseEntity
     */
    @GetMapping("/")
    public ResponseEntity getAllBooks() {
        try {
            DefaultRes<List<BookRes>> defaultRes = listService.findAllBook();
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 검색된 판매 도서 목록 조회
     *
     * @return ResponseEntity
     */
    @RequestMapping("searches")
    public ResponseEntity getSearchedBooks(@RequestParam(value="keyword", defaultValue="") String keyword) {
        try {
            DefaultRes<List<BookRes>> defaultRes = listService.findSearchedBook(keyword);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 판매 도서 상세 조회
     *
     * @return ResponseEntity
     */
    @GetMapping("adv")
    public ResponseEntity getSearchedBooks(@RequestParam(value="bookId", defaultValue="") int bookId) {
        try {
            DefaultRes<BookRes> defaultRes = listService.findAdvancedBook(bookId);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 판매 도서 등록
     *
     * @param bookReq 책 데이터
     * @return ResponseEntity
     */
    @PostMapping("sell")
    public ResponseEntity saveBook(@RequestBody final BookReq bookReq) {
        try {
            return new ResponseEntity<>(listService.saveBook(bookReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 도서 구매 확정
     *
     * @param transactionReq 거래 데이터
     * @return ResponseEntity
     */
    @PostMapping("buy")
    public ResponseEntity saveTransactinon(@RequestBody final TransactionReq transactionReq) {
        try {
            listService.updateSellTransaction(transactionReq.getBookId());
            return new ResponseEntity<>(listService.saveTransaction(transactionReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
