package jungsuk.ch14;

import java.io.File;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// ch.14 p.823 중간 연산
public class Stream02IntermediateOperation_Exs {

    static Student[] students = {
            new Student("이자바", 3, 300),
            new Student("김자바", 1, 200),
            new Student("안자바", 2, 100),
            new Student("박자바", 2, 150),
            new Student("소자바", 1, 200),
            new Student("나자바", 3, 290),
            new Student("감자바", 3, 180)
    };

    // p.826 예제 14-8
    public void sortedEx() {
        System.out.println("sortedEx()");

        Stream<Student> studentStream = Stream.of(students);
        studentStream.sorted(Comparator.comparingInt(Student::getBan) // 반별 정렬
                        .thenComparing(Comparator.naturalOrder())) // 기본 정렬
                .forEach(System.out::println);

        System.out.println("============================================================");
    }

    static class Student implements Comparable<Student> {

        String name;
        int ban;
        int totalScore;

        public Student(String name, int ban, int totalScore) {
            this.name = name;
            this.ban = ban;
            this.totalScore = totalScore;
        }

        @Override
        public String toString() {
            return String.format("[%s, %d, %d]", name, ban, totalScore);
        }

        public String getName() {
            return name;
        }

        public int getBan() {
            return ban;
        }

        public int getTotalScore() {
            return totalScore;
        }
        
        @Override
        // Student의 기본 정렬은 totalScore 내림차순
        public int compareTo(Student other) {
            return other.totalScore - this.totalScore;
        }
    }

    // p.828 예제 14-9
    public void mapEx() {
        System.out.println("mapEx()");

        File[] fileArr = {new File("Ex1.java"), new File("Ex1"),
                new File("Ex1.bak"), new File("Ex2.java"), new File("Ex1.txt")};

        // 작업 1
        Stream<File> fileStream = Stream.of(fileArr);

        Stream<String> filenameStream = fileStream.map(File::getName); // map()으로 Stream<File>을 Stream<String>으로 변환
        filenameStream.forEach(System.out::println); // 모든 파일의 이름을 출력

        // 작업 2
        fileStream = Stream.of(fileArr); // 스트림 다시 생성
        fileStream.map(File::getName) // Stream<File> -> Stream<String>
                .filter(s -> s.indexOf('.') != -1)
                .map(s -> s.substring(s.indexOf('.') + 1)) // 확장자만 추출
                .map(String::toUpperCase) // 모두 대문자로 변환
                .distinct() // 중복 제거
                .forEach(System.out::print); // -> JAVABAKTXT
        System.out.println();

        System.out.println("============================================================");
    }

    // p.830 예제 14-10
    // Stream03TerminalOperation.class mapToIntLongDouble_summaryStatistics()와 같은 내용
    public void summaryStatisticsEx() {
        System.out.println("summaryStatisticsEx()");

        Stream<Student> studentStream = Stream.of(students);
        studentStream.sorted(Comparator.comparing(Student::getBan).thenComparing(Comparator.naturalOrder()))
                // Comparable이더라도, 이렇게 thenComparing(~) 같은 곳에서 추가로 사용할 수 있기 때문에, naturalOrder() 같은 메서드를 갖고 있다고 생각할 수 있겠다.
                .forEach(System.out::println);

        studentStream = Stream.of(students); // 스트림을 다시 생성한다. - 기존에 studentStream은 이미 닫혔으므로 사용할 수 없음
        IntStream studentScoreStream = studentStream.mapToInt(Student::getTotalScore);

        IntSummaryStatistics stat = studentScoreStream.summaryStatistics();
        System.out.printf("""
                count = %d
                sum = %d
                average = %.2f
                min = %d
                max = %d
                """, stat.getCount(), stat.getSum(), stat.getAverage(), stat.getMin(), stat.getMax());
        System.out.println("============================================================");
    }

    // p.834 예제 14-11
    public void flatMap() {
        System.out.println("flatMap()");
        System.out.println();

        Stream<String[]> strArrStream = Stream.of(new String[]{"abc", "def", "jkl"}, new String[]{"ABC", "GHI", "JKL"});

        // 아래처럼 하면 안 된다.
        // Stream<Stream<String>> streamStream = strArrStream.map(array -> Arrays.stream(array));
        // strArrStream의 요소는 String[]
        // => strArrStream.map(Arrays::stream)은 strArrStream의 각 요소인 String[] strArr1, String[] strArr2, ...에 대해서
        //    Arrays.stream(strArr1), Arrays.stream(strArr2), ... 을 요소로 하는 스트림을 만드는 것이 됨
        // => Arrays.stream(strArr1), ... 는 Stream<String> 타입이 되므로 결과적으로
        //    Stream<Stream<String>> 타입이 된다.
        Stream<String> strStream = strArrStream.flatMap(Arrays::stream);

        strStream.map(String::toLowerCase).distinct().sorted().forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
        String[] lineArr = {
                "Believe or not It is true",
                "Do or do not There is no try",

        };
        Stream<String> lineStream = Arrays.stream(lineArr);

        lineStream.flatMap(line -> Stream.of(line.split(" +"))) // cf. 정규표현식 " +" => 공백 문자 하나 혹은 여러 개(+는 앞의 문자 한 번 혹은 여러 번을 뜻함)
                        .map(String::toLowerCase).distinct().sorted().forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
        // 드문 경우이지만, 스트림의 스트림을 하나로 합칠 때도 flatMap()을 사용
        Stream<String> strStream1 = Stream.of("AAA", "ABC", "bBb", "Dd");
        Stream<String> strStream2 = Stream.of("bbb", "aaa", "ccc", "dd");

        Stream<Stream<String>> streamOfStringStream = Stream.of(strStream1, strStream2);
        Stream<String> finalStringStream = streamOfStringStream
                .map(stringStream -> stringStream.toArray(String[]::new)) // Stream<String>을 String[]로 바꿔주었다. ==> Stream<Stream<String>>에서 Stream<String[]>이 된 상태
                .flatMap(Arrays::stream);

        finalStringStream.map(String::toLowerCase).distinct().forEach(System.out::println);

        System.out.println("============================================================");
    }
}
