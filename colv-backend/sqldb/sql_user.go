package sqldb

import (
	"fmt"
	"time"

	"golang.org/x/crypto/bcrypt"
)

const userTableName = "users"

type User struct {
	UID        uint      `gorm:"primaryKey;column:user_UID"`
	Mail       string    `gorm:"column:user_mail"`
	UserName   string    `gorm:"column:user_name"`
	PwdHash    string    `gorm:"column:passwd_hash"`
	CreateTime time.Time `gorm:"column:create_time"`
}

func (db *MysqlDB) UserSignUp(inputName string, inputMail string, inputPwd string) error {
	var exists bool
	// 查询是否有匹配的记录
	result := db.DB.Table(userTableName).Select("1").Where("user_mail = ?", inputMail).Limit(1).Find(&exists)
	if result.RowsAffected > 0 {
		return fmt.Errorf("existed mail")
	}

	// 生成 bcrypt 哈希
	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(inputPwd), bcrypt.DefaultCost)
	if err != nil {
		return err
	}

	// 向 DB 中写入账户
	createResult := db.DB.Table(userTableName).Create(
		&User{
			Mail:       inputMail,
			UserName:   inputName,
			PwdHash:    string(hashedPassword),
			CreateTime: time.Now(),
		},
	)
	return createResult.Error
}

func (db *MysqlDB) UserLogin(inputMail string, inputPwd string) (*User, error) {
	var userFromDB User

	// 检验数据库中是否存在 User
	result := db.DB.Table(userTableName).Where("user_mail = ?", inputMail).First(&userFromDB)
	if result.Error != nil {
		return nil, result.Error
	}

	// 校验密码
	if !checkPasswordHash(userFromDB.PwdHash, inputPwd) {
		return nil, fmt.Errorf("invalid password")
	}

	return &userFromDB, nil
}

func checkPasswordHash(pwdHash, inputPwd string) bool {
	return bcrypt.CompareHashAndPassword([]byte(pwdHash), []byte(inputPwd)) == nil
}
