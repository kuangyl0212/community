package com.jnucc.community.constant;

public enum UserState {
    OK("success", 0),
    USERNAME_EXIT("username exit", 1),
    EMAIL_EXIT("email exit", 2),
    ACT_CODE_WRONG("activation code wrong", 3),
    ID_NOT_EXIT("user not exit", 4),
    USERNAME_BLANK("username is empty", 5),
    EMAIL_BLANK("email is empty", 6),
    PASSWORD_BLANK("password is empty", 7),
    USERNAME_NOT_EXIT("username not exit", 8),
    INACTIVATED("inactivated", 9),
    WRONG_PASSWORD("wrong password", 10);

    private String msg;
    private int index;

    private UserState(String msg, int index) {
        this.msg = msg;
        this.index = index;
    }

    public String getMsg() {
        return msg;
    }
}
