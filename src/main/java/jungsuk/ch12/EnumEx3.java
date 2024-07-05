package jungsuk.ch12;

public class EnumEx3 {

    public void ex3() {
        System.out.println("bus distance 10 fare = " + Transportation.BUS.getFare(10));
        System.out.println("bus distance 100 fare = " + Transportation.BUS.getFare(100));
        System.out.println("train distance 100 fare = " + Transportation.TRAIN.getFare(100));
        System.out.println("ship distance 100 fare = " + Transportation.SHIP.getFare(100));
        System.out.println("airplane distance 100 fare = " + Transportation.AIRPLANE.getFare(100));
    }

    enum Transportation {
        BUS(100) {
            @Override
            int getFare(int distance) {
                return distance * super.basicFare; // super 가능
            }
        }, TRAIN(150) {
            @Override
            int getFare(int distance) {
//                return distance * this.basicFare; // basicFare에 private을 사용하는 경우 this는 사용 불가
                return distance * getBasicFare(); // super.basicFare를 가져오는 방식으로 우회 가능
            }
        }, SHIP(100) {
            @Override
            int getFare(int distance) {
                return distance * getBasicFare();
            }
        }, AIRPLANE(300) {
            @Override
            int getFare(int distance) {
                return distance * getBasicFare();
            }
        };

        // private final int basicFare; // abstract method 때문에 Transportation의 subclass에서 이 필드에 접근할 수 있어야하므로 private일 수 없음   
        // protected final int basicFare; // 이쪽이 자연스러울 수 있음
        private final int basicFare;

        Transportation(int basicFare) {
            this.basicFare = basicFare; // 상속하는 subclass 입장에서는 super()를 호출하는 꼴이 것
        }

        public int getBasicFare() {
            return basicFare; // subclass에서 호출 시 override하지 않았다면 super.getBasicFare()가 될 것, 자연스럽게 super.basicFare를 가져옴
        }

        abstract int getFare(int distance); // 거리에 따른 요금 계산
    }
}
