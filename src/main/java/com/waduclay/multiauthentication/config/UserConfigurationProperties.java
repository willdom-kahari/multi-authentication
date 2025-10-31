package com.waduclay.multiauthentication.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@ConfigurationProperties(prefix = "user")
public class UserConfigurationProperties {
    private AppUser one;
    private AppUser two;

    public AppUser getOne() {
        return one;
    }

    public void setOne(AppUser one) {
        this.one = one;
    }

    public AppUser getTwo() {
        return two;
    }

    public void setTwo(AppUser two) {
        this.two = two;
    }

    public static class AppUser{
        private String name;
        private String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
