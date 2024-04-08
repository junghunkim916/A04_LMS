//각 process는 생성자를 통해 오늘 날짜(todayDate)를 받아온다.
//todayDate는 사용자가 입력한 날짜를 의미하며, 각 년도, 월, 일은 공백으로 구분 (EX. 2023 02 01)
//모든 Process는 BookDAO를 통해 파일에서 데이터를 읽어오고, 데이터를 파일에 쓴다.
//BookDAO의 getDataFromFiles() 메소드는 bookList.txt 파일에서 데이터를 읽어와서 ArrayList<BookVO> 형태로 반환한다.
//ArrayList<BookVO>를 통해서 각 도서에 접근하고, 도서 정보를 수정 할 수 있다.
//각 Process는 특정 작업을 마치면, BookDAO의 writeDataToFiles(ArrayList<BookVO> bookList) 메소드를 호출하여, 변경된 데이터를 txt파일에 기록한다.

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Process2 {

    int menu;
    private String todayDate;
    private String process_input; //메뉴 입력값
    Scanner scanner; // 스캐너 메인 메뉴와 통일 필요성?
    public Process2(String todayDate) {

        this.todayDate = todayDate;

        while (true) {
            System.out.println("> A04 LMS");
            System.out.println("1) Search by Title (제목 검색)");
            System.out.println("2) Search by Author (저자 검색)");
            System.out.println("3) Previous (뒤로 가기)");
            System.out.println("------------------------------------------------------------");
            System.out.print("> A04 LMS: Search books > ");
            scanner = new Scanner(System.in);

            try {
                process_input = scanner.nextLine();
                if (Integer.parseInt(process_input) != 3)
                    System.out.println("------------------------------------------------------------");
                if (isValid_MenuInput(process_input)) {
                    menu = Integer.parseInt(process_input);

                    if (menu == 1)
                        searchByTitle();
                    else if (menu == 2)
                        searchByAuthor();
                    else if (menu == 3)
                        break;
                    else {
                        System.out.println("잘못 입력했습니다. 범위(1~3) 안에서 다시 선택해주세요");
                        System.out.println("------------------------------------------------------------");
                    }
                } else {
                    System.out.println("잘못 입력했습니다. 범위(1~3) 안에서 다시 선택해주세요");
                    System.out.println("------------------------------------------------------------");
                }
            } catch (Exception e) {
                System.out.println("------------------------------------------------------------");
                System.out.println("잘못 입력했습니다. 범위(1~3) 안에서 다시 선택해주세요");
                System.out.println("------------------------------------------------------------");
            }
        }
    }

    private void searchByTitle() {
        BookDAO bookDAO = new BookDAO(); // 임시 bookDAO - 공동으로 사용하는 bookDAO가 있으면 그것을 가져와야 함
        ArrayList<BookVO> bookList = bookDAO.getDataFromFiles();

        boolean success = false; // 검색 결과가 있을 때까지 반복

        do {
            System.out.println("> A04 LMS");
            System.out.println("검색할 도서의 제목 일부분을 입력하세요.");
            System.out.println("------------------------------------------------------------");
            System.out.print("> A04 LMS: Search by Title > ");
            process_input = scanner.nextLine();

            if(!isValid_ProcessInput(process_input)) {
                System.out.println("검색 결과가 존재하지 않습니다.");
            } else {
                List<BookVO> filteredBooks = bookList.stream()
                        .filter(book -> book.getTitle().contains(process_input))
                        .toList();

                if (filteredBooks.isEmpty()) {
                    System.out.println("검색 결과가 존재하지 않습니다.");
                } else {
                    success = true;
                    int bookNum = 1;
                    System.out.println("  제목/ 저자 / 등록날짜 / 인덱스 / 위치 / 대여기간");
                    for (BookVO book : filteredBooks) {
                        //System.out.println((bookNum++) + ")" + book.toBookFileString());
                        if(book.getCurrentRecord() == null) {
                            System.out.println((bookNum++) + ")" + book.toBookFileString() + "/대여가능");
                        }else{
                            System.out.println((bookNum++) + ")" + book.toBookFileStringWithoutSno());
                        }
                    }
                }
            }
            System.out.println("------------------------------------------------------------");
        } while(!success);
    }

    private void searchByAuthor() {
        BookDAO bookDAO = new BookDAO(); // 임시 bookDAO - 공동으로 사용하는 bookDAO가 있으면 그것을 가져와야 함
        ArrayList<BookVO> bookList = bookDAO.getDataFromFiles();

        boolean success = false; // 검색 결과가 있을 때까지 반복

        do {
            System.out.println("> A04 LMS");
            System.out.println("검색할 도서의 저자 일부분을 입력하세요.");
            System.out.println("------------------------------------------------------------");
            System.out.print("> A04 LMS: Search by Author > ");
            process_input = scanner.nextLine();

            if(!isValid_ProcessInput(process_input)) {
                System.out.println("해당 저자의 도서를 찾을 수 없습니다.");
            } else {
                List<BookVO> filteredBooks = bookList.stream()
                        .filter(book -> book.getAuthor().contains(process_input))
                        .toList();

                if (filteredBooks.isEmpty()) {
                    System.out.println("해당 저자의 도서를 찾을 수 없습니다.");
                } else {
                    success = true;
                    int bookNum = 1;
                    System.out.println("  제목 / 저자 / 등록날짜 / 인덱스 / 위치 / 대여기간");
                    for (BookVO book : filteredBooks) {
                        //System.out.println((bookNum++) + ")" + book.toBookFileString());
                        if(book.getCurrentRecord() == null) {
                            System.out.println((bookNum++) + ")" + book.toBookFileString() + "/대여가능");
                        }else{
                            System.out.println((bookNum++) + ")" + book.toBookFileStringWithoutSno());
                        }
                    }
                }
            }
            System.out.println("------------------------------------------------------------");
        } while(!success);
    }

    private boolean isValid_MenuInput(String e) {
        if (e.length() != 1) {
            e = e.trim();
            if (e.length() != 1)
                return false;
            else {
                int analysis = Integer.parseInt(e);
                if (analysis < 1 || analysis > 3)
                    return false;
                else
                    this.process_input = Integer.toString(analysis);
                return true;
            }
        }
        int analysis = Integer.parseInt(e);
        return analysis >= 1 && analysis <= 3;
    }

    private boolean isValid_ProcessInput(String e) {
        return !e.trim().isEmpty();
    }
}
