package jungsuk.ch04;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

// ch04 p.136 control statement: 제어문-Java 14 switch, for 초기화 식 등, enhanced for 주의, 이름 붙은 반복문
public class ControlStatement {

    /*
     * control statement(제어문)
     * - 조건문, 반복문
     * - (코드의 실행 흐름이 위에서 아래로 한 문장씩 순차적으로 진행되는 것을 벗어나서) 조건에 따라 문장을 건너 뛰고, 때로는 같은 문장을 반복해서 수행
     *   => 프로그램의 흐름(flow)을 바꾸는 역할을 하는 문장
     *
     * 조건문의 구성: 조건식 + 문장을 포함하는 블럭 {}
     * - 조건식의 연산 결과에 따라 실행할 문장이 달라져서 프로그램의 실행흐름을 변경할 수 있다.
     * - Java에서 if문의 조건식의 결과는 boolean이어야 함(cf. JavaScript에서는 boolean이 아닐 수 있음)
     * - switch문은 단 하나의 조건식으로 많은 경우의 수를 처리할 수 있지만, 제약조건이 있다.
     * 
     * 반복문의 구성: (1) for문 - 초기화 + 조건식 + 증감식 + 블럭  (2) while문 - 조건식 + 블럭(조건식이 true인 동안 반복)
     */

    // if 예제 따로 기록하지 않음 // Java 14 이상 switch 달라진 부분만 참고
    public void conditional_if_switch() {
        // cf. Java 14 이상 switch 달라진 부분
        // - https://mostadmired.tistory.com/127 - [PMJ] Practical 모던 자바 - 스위치 표현식 (switch expressions)
        //   - (cf.) "마틴 파울러의 Refactoring 책에 의하면 switch 문장은 구린내 나는 코드로 정의하였고 switch 문장 대신 재정의를 통해 case로 구분하는 일이 없도록 하라고 권하고 있다."
        // - https://velog.io/@nunddu/Java-Switch-Expression-in-Java-14 - Switch Expression in Java 14
        // Java 17 spec.
        // - (1) 14.11 https://docs.oracle.com/javase/specs/jls/se17/html/jls-14.html#jls-14.11
        // - (2) 15.28 https://docs.oracle.com/javase/specs/jls/se17/html/jls-15.html#jls-15.28
        System.out.println("conditional_if_switch()");

        System.out.println("if 예제 따로 기록하지 않음");
        System.out.println("------------------------------------------------------------");
        System.out.println("Java 14 이상 switch 달라진 부분");
        System.out.print("요일을 영문 소문자로 입력 >>> ");

        /*
        try (Scanner sc = new Scanner(System.in)) {
        } catch (IllegalArgumentException e) {
        }
        // scanner 인스턴스를 메서드 안에서 close() 하면, 다른 메서드에서 scanner 쓰려고 시도할 때 NoSuchElementException 발생해서 일단 닫지 않음
        // 한 번만 돌리는 예제이므로 크게 상관은 없다.
         */
        Scanner sc = new Scanner(System.in);
        try {
            String dayString = sc.next();
            DAY day = DAY.getDayByDayString(dayString);

            int intByDayEnum = switch (day) {
                case MONDAY, FRIDAY -> 6;
                case TUESDAY -> 7;
                case WEDNESDAY, THURSDAY-> 8;
                default -> {
                    int i = day.toString().length();
                    yield i + 3;
                }
            };
            System.out.println("dayEnum: " + day + ", intByDayEnum: " + intByDayEnum);

        } catch (IllegalArgumentException e) {
            System.out.println("잘못된 영문 요일 입력");
        }
        // sc.close();
        // scanner 인스턴스를 메서드 안에서 close() 하면, 다른 메서드에서 scanner 쓰려고 시도할 때 NoSuchElementException 발생해서 일단 닫지 않음
        // 한 번만 돌리는 예제이므로 크게 상관은 없다.
        System.out.println("============================================================");
    }

    // conditional_if_switch() 실행을 위한 추가 enum
    private enum DAY {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

        public static DAY getDayByDayString(String dayString) {
            return switch (dayString) {
                case "monday" -> MONDAY;
                case "tuesday" -> TUESDAY;
                case "wednesday" -> WEDNESDAY;
                case "thursday" -> THURSDAY;
                case "friday" -> FRIDAY;
                case "saturday" -> SATURDAY;
                case "sunday" -> SUNDAY;
                default -> throw new IllegalArgumentException("Unexpected value: " + dayString);
            };
        }
    }

    // p.157, 158 내용, p.159 예제 4-14 참고
    public void iteration_for_enhancedFor_while_do() {
        System.out.println("iteration_for_enhancedFor_while_do()");
        // 초기화, 조건식, 증감식 모두 생략된 for문 - 보통 이렇게 생략되는 경우라면 for보다는 while을 사용하는 편이 더 적절한 경우일 것 같다.
        boolean flag = false;
        int count = 0;
        for (; ; ) {
            System.out.println("count = " + count++);
            if (count == 5) {
                flag = true;
                System.out.println("flag: " + flag);
                break;
            }
        }

        // 제어용 변수가 두 개 이상 선언된 for문(증감식 둘) - 두 제어용 변수의 타입은 같아야 함
        for (int i = 1, j = 10; i <= 5; i++, j--) {
            System.out.printf("i = %d \t j = %d%n", i, j);
        }

        // 제어용 변수가 두 개 선언된 for문(증감식은 하나) - 선언만 하고 초기화는 안 할 수도 있음
        for (int i = 0, j; i <= 5; i++) {
            j = i * 2;
            System.out.printf("i = %d \t j = %d%n", i, j);
        }

        System.out.println("------------------------------------------------------------");

        // enhanced for - Java 1.5부터
        // cf. https://docs.oracle.com/javase/specs/jls/se17/html/jls-14.html#jls-14.14.2
        System.out.println("enhanced for 유의사항 - enhanced for의 각 원소들은 읽을 수는 있지만 수정할 수는 없음");
        String[] strings = new String[3]; // (IDE 경고) 배열 'strings'의 내용은 읽기는 되지만 쓰기는 되지 않습니다
        for (String string : strings) {
            string = "0";
        }
        for (String string : strings) {
            System.out.println(string);
        }

        // enhanced for는 사실 다음을 축약한 것이다. (https://docs.oracle.com/javase/specs/jls/se17/html/jls-14.html#jls-14.14.2)
        for (Iterator<String> iterator = Arrays.stream(strings).iterator(); iterator.hasNext();) {
            String string = (String) iterator.next();
            string = "0";
        }
        for (String string : strings) {
            System.out.println(string);
        }
        // 원본 데이터를 iterator로 가져오기 때문에(정확하게는 Spliterators의 Adapter 인스턴스), 원본 데이터에는 영향이 없다.

        System.out.println("------------------------------------------------------------");
        
        System.out.println("이름 붙은 반복문 - 중첩 반복문에서 하나 이상의 반복문을 벗어나거나 건너뛰는 용도");
        int menu = 0, num = 0;
        Scanner scanner = new Scanner(System.in);

        outer: // 반복문의 이름 지정
        while (true) {
            System.out.println("(1) square");
            System.out.println("(2) square root");
            System.out.println("(3) log");
            System.out.print("원하는 메뉴 (1~3)를 선택하세요. (종료: 0) >>> ");

            menu = scanner.nextInt();

            if (menu == 0) {
                System.out.println("프로그램을 종료합니다.");
                break;
            } else if (!(1 <= menu && menu <= 3)) {
                System.out.println("메뉴를 잘못 선택하셨습니다. (종료는 0)");
                continue;
            }

            for (; ; ) { // 초기화, 조건식, 증감식 모두 없는 for문
                System.out.print("계산할 값을 입력하세요. (계산 종료: 0, 전체 종료: 99) >>> ");
                num = scanner.nextInt();
                
                if (num == 0) {
                    break; // 계산 종료. for문을 벗어난다. while문 시작
                }

                if (num == 99) {
                    break outer; // 전체 종료. for문과 while문을 모두 벗어난다.
                }

                switch (menu) {
                    case 1 -> System.out.println("result(square) = " + num * num);
                    case 2 -> System.out.println("result(square root) = " + Math.sqrt(num));
                    case 3 -> System.out.println("result(log) = " + Math.log(num));
                }
            }
        }
        // scanner.close(); // while문 벗어나면 자원 닫고 종료
        // scanner 인스턴스를 메서드 안에서 close() 하면, 다른 메서드에서 scanner 쓰려고 시도할 때 NoSuchElementException 발생해서 일단 닫지 않음
        // 한 번만 돌리는 예제이므로 크게 상관은 없다.

        System.out.println("============================================================");
    }
}
