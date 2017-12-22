package com.argus.enummj;

/**
 * Created by xingding on 2017/4/8.
 * 枚举策略
 */
public enum PayrollDayEnum {
    MONDAY(PayTypeEnum.WEEKDAY),
    TUESDAY(PayTypeEnum.WEEKDAY),
    WEDNESDAY(PayTypeEnum.WEEKDAY),
    THURSDAY(PayTypeEnum.WEEKDAY),
    FRIDAY(PayTypeEnum.WEEKDAY),
    SATURDAY(PayTypeEnum.WEEKEND),
    SUNDAY(PayTypeEnum.WEEKEND);
    private PayTypeEnum payTypeEnum;
    PayrollDayEnum(PayTypeEnum payTypeEnum){
        this.payTypeEnum = payTypeEnum;
    }
    double pay(double hoursWorked, double payRate){
        return payTypeEnum.pay(hoursWorked,payRate);
    }

    private enum PayTypeEnum{
        WEEKDAY{
            @Override
            double overtimePay(double hrs, double payRate) {
                return hrs <= HOUR_PER_SHIFT?0:(hrs-HOUR_PER_SHIFT)*payRate/2;
            }
        },
        WEEKEND{
            @Override
            double overtimePay(double hrs, double payRate) {
                return hrs*payRate/2;
            }
        };

        private static final int HOUR_PER_SHIFT = 8;
        abstract double overtimePay(double hrs, double payRate);

        double pay(double hoursWorked, double payRate){
            double basePay = hoursWorked * payRate;
            return basePay + overtimePay(hoursWorked, payRate);
        }
    }

}
