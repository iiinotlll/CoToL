package sqldb

import (
	"fmt"
	"testing"
)

func TestUserSignUp(t *testing.T) {
	db := MysqlDBInit()
	if err := db.UserSignUp("DDDD", "testUse129r@test18.com", "thisisApswd"); err != nil {
		fmt.Println(err.Error())
	} else {
		fmt.Println("Passed")
	}
}

func TestUserLogin(t *testing.T) {
	db := MysqlDBInit()
	fmt.Println(db.UserLogin("testUse129r@test18.com", "thisisApswd"))
}
