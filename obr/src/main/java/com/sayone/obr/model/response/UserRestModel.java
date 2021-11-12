package com.sayone.obr.model.response;

public class UserRestModel {

        private String firstName;
        private String lastName;
        private String email;
        private long phoneNumber;
        private String userStatus;
        private String role;

        public String getRole() {
                return role;
        }

        public void setRole(String role) {
                this.role = role;
        }

        public String getFirstName() {
                return firstName;
        }

        public void setFirstName(String firstName) {
                this.firstName = firstName;
        }

        public String getLastName() {
                return lastName;
        }

        public void setLastName(String lastName) {
                this.lastName = lastName;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public long getPhoneNumber() {
                return phoneNumber;
        }

        public void setPhoneNumber(long phoneNumber) {
                this.phoneNumber = phoneNumber;
        }

        public String getUserStatus() {
                return userStatus;
        }

        public void setUserStatus(String userStatus) {
                this.userStatus = userStatus;
        }
}
