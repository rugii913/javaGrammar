package jungsuk.ch14;

// ch.14 p.799 람다식
// 예제에서는 MyFunction이라는 인터페이스를 따로 정의했으나, 굳이 그럴 필요는 없어서 Runnable 사용
public class Lambda01Basic {
    /*
     * ***** functional interface 타입이 들어갈 자리에 람다식을 넣을 수 있다고 생각하면 된다. *****
     */

    // p.799 예제 14-1
    public void lambdaExpression() {
        System.out.println("lambdaExpression()");

        // 람다식으로 Runnable의 run() 구현
        Runnable runnable1 = () -> System.out.println("runnable1.run()");
        // 위는 아래 코드에서 생략이 된 것
        // Runnable runnable1 = (Runnable) (() -> System.out.println("runnable1.run()"));
        // (p.800)
        // - 람다식은 익명 객체이고, 익명 객체는 타입이 없으나,
        // - 람다식이 Runnable을 구현한 클래스의 객체와 완전히 동일하기 때문에 위와 같은 형변환을 허용하는 것이고, 이 형변환은 생략된 것이라고 설명하고 있다.
        // Runnable runnable10 = (Object) (() -> System.out.println("runnable1.run()"));
        // 위와 같은 코드는 컴파일 에러: "람다 변환의 타깃 타입은 인터페이스여야 합니다."
        // (p.800) 람다식은 이름이 없을 뿐 분명히 객체인데도, Object 타입으로 형변환 할 수 없다. 람다식은 오직 함수형 인터페이스로만 형변환 가능하다.
        // -> 문법적인 규칙이라고 보면 될 것
        runnable1.run();

        System.out.println("------------------------------------------------------------");

        // 익명 클래스로 run() 구현
        Runnable runnable2 = new Runnable() { // (IDE 교정) 익명의 new Runnable() 람다로 바꿀수 있습니다.
            @Override
            public void run() { // public이 반드시 있어야 함 - @Override에서 접근제어자의 범위가 더 좁아질 수는 없으므로
                System.out.println("runnable2.run()");
            }
        };
        runnable2.run();

        System.out.println("------------------------------------------------------------");

        // Runnable을 반환하는 메서드에서 Runnable을 얻어옴(람다식으로 Runnable이 구현되어 있는 상태)
        Runnable runnable3 = getRunnable();
        runnable3.run();

        System.out.println("------------------------------------------------------------");

        System.out.println("각각 execute");
        execute(runnable1);
        execute(runnable2);
        execute(runnable3);
        execute(() -> System.out.println("runnable4.run() - functional interface Runnable 타입을 매개변수로 받는 자리에 바로 람다식을 넣었다."));
        System.out.println("============================================================");
    }

    private void execute(Runnable runnable) { // 매개변수의 타입이 functional interface인 메서드
        runnable.run();
    }

    private Runnable getRunnable() { // 반환 타입이 functional interface인 메서드
        return () -> System.out.println("runnable3.run()");
    }

    // p.800 예제 14-2
    public void lambdaInstanceAndCasting() {
        System.out.println("lambdaInstanceAndCasting()");

        // lambdaEx1() 앞부분 이어서
        // (p.800)
        // - 람다식은 익명 객체이고, 익명 객체는 타입이 없으나,
        // - 람다식이 Runnable을 구현한 클래스의 객체와 완전히 동일하기 때문에 위와 같은 형변환을 허용하는 것이고, 이 형변환은 생략된 것이라고 설명하고 있다.
        // Runnable runnable10 = (Object) (() -> System.out.println("runnable1.run()"));
        // 위와 같은 코드는 컴파일 에러: "람다 변환의 타깃 타입은 인터페이스여야 합니다."
        // (p.800) 람다식은 이름이 없을 뿐 분명히 객체인데도, Object 타입으로 형변환 할 수 없다. 람다식은 오직 함수형 인터페이스로만 형변환 가능하다.
        // -> 문법적인 규칙이라고 보면 될 것
        Runnable runnable = (Runnable) (() -> {});      // 여기서 생략이 되면 Runnable runnable = () -> {}
        // Object obj0 = (Object) (() -> {});           // (컴파일 에러) 람다 변환의 타깃 타입은 인터페이스여야 합니다.
        Object obj1 = (Object) ((Runnable) (() -> {}));
        Object obj2 = (Object) (Runnable) (() -> {});   // 연산 순서상 위와 같음
        Object obj3 = (Runnable) (() -> {});            // Obejct 타입으로의 형변환 (Object)가 생략됨
        String str = ((Object) (Runnable) (() -> {})).toString();

        System.out.println(runnable);   // jungsuk.ch14.LambdaEx$$Lambda$20/0x0000000800c03430@568db2f2
        System.out.println(obj1);       // jungsuk.ch14.LambdaEx$$Lambda$21/0x0000000800c03648@378bf509
        System.out.println(obj2);       // jungsuk.ch14.LambdaEx$$Lambda$22/0x0000000800c03860@5fd0d5ae
        System.out.println(obj3);       // jungsuk.ch14.LambdaEx$$Lambda$23/0x0000000800c03a78@2d98a335
        System.out.println(str);        // jungsuk.ch14.LambdaEx$$Lambda$24/0x0000000800c03c90@6d03e736

        // System.out.println(() -> {});
        // => (컴파일 에러) 'println'의 메서드 'println'을(를) 해결할 수 없습니다
        // System.out.println((Object) (() -> {}));
        // => (컴파일 에러) 람다 변환의 타깃 타입은 인터페이스여야 합니다.
        System.out.println((Runnable) (() -> {}));                       // jungsuk.ch14.LambdaEx$$Lambda$25/0x0000000800c02800@7ef20235
        System.out.println((Object) ((Runnable) (() -> {})));            // jungsuk.ch14.LambdaEx$$Lambda$27/0x0000000800c02a18@4f3f5b24
        // => cf. (IDE 교정) ((Runnable)(() -> {...}))'을(를) 'Object'(으)로 형 변환하는 것은 불필요합니다
        // System.out.println((() -> {}).toString());
        // => (컴파일 에러) 메서드 'println(?)'를 해결할 수 없습니다, 람다 식은 필요하지 않습니다
        // System.out.println((Runnable) (() -> {}).toString());
        // => (컴파일 에러) 람다 식은 필요하지 않습니다
        System.out.println(((Object) (Runnable) (() -> {})).toString()); // jungsuk.ch14.LambdaEx$$Lambda$28/0x0000000800c02c30@7b23ec81
        // => cf. (IDE 교정) 'toString()' 호출이 불필요합니다
        System.out.println("""
                출력 결과를 보면 일반적인 익명 객체와도 다름을 알 수 있음
                  - 일반적인 익명 객체 toString 출력 시: '외부클래스이름$번호' 형식
                  - 람다식의 toString 출력 시:         '외부클래스이름$$Lambda$번호' 형식""");
        System.out.println("============================================================");
    }

    // p.801 예제 14-3 -> p.409 익명 클래스 예제 7-35를 람다식을 사용한 방식으로 변경
    // 람다식이 외부에 선언된 변수에 접근하는 규칙: 익명 클래스에 적용되는 규칙과 동일
    // TODO: 클래스 부분 복습
    public void lambdaReferenceRule() {
        System.out.println("lambdaReferenceRule()");
        // 아래 Outer, Inner 클래스 참고
        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner();
        // cf. Inner를 static class로 만들었을 경우 new Outer.Inner();와 같은 방식으로 만들어야 함, // static class일 경우 Inner 코드 중 ++Outer.this 부분에서 컴파일 에러

        inner.method(100);

        System.out.println("============================================================");
    }
}

// LambdaBasic 클래스 - lambdaReferenceRule() 메서드 관련 클래스
class Outer {

    int val = 10; // Outer.this.val

    class Inner {

        int val = 20; // this.val

        void method(int i) { // void vethod(final int i)
            int val = 30; // final int val = 30;
            // i = 0; // 여기서 i의 값을 바꾸려고 하면 아래 람다식에서 컴파일 에러
            // ! 람다식 내에서 참조하는 지역 변수는 final이 붙지 않았어도 상수로 간주(유사 final) - 여기서는 i와 val => 변경하려고 하면 컴파일 에러

            Runnable runnable = () -> {
                System.out.println("             i: " + i); // 위에서 i = 0;으로 값을 바꾼 경우 (컴파일 에러) 람다 표현식에 사용되는 변수는 final 또는 유사 final이어야 합니다
                System.out.println("           val: " + val);
                System.out.println("           val: " + ++this.val);
                System.out.println("Outer.this.val: " + ++Outer.this.val);
            };

            runnable.run();
        }
    }
}
