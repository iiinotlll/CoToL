package sqldb

import (
	"fmt"
	"log"

	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

type MysqlDB struct {
	DB *gorm.DB
}

func MysqlDBInit() *MysqlDB {
	dsn := "root:root@tcp(localhost:3306)/colv-db?charset=utf8mb4&parseTime=True&loc=Local"
	database, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		log.Fatalf("failed to connect to MySQL: %v", err)
	}
	fmt.Println("Connected to MySQL")
	return &MysqlDB{database}
}
