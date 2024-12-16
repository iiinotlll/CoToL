package sqldb

import (
	"fmt"
	"testing"
)

func TestArticlePost(t *testing.T) {
	db := MysqlDBInit()
	uid := uint(10)
	title := "TestTitle"
	data := "精心整理了各个促销节日方案合集、行业营销方案合集、品牌营销方案合集、知名品牌营销方案合集、行业报告期刊合集，已有超千人收藏！团队管理者必看，涵盖人、财、事统一管理，实现部门人员、预算、重点项目等资源的集中管理。"

	if err := db.PostArticle(uid, title, data); err != nil {
		fmt.Println(err.Error())
	} else {
		fmt.Println("Passed")
	}
}

func TestArticleGet(t *testing.T) {
	db := MysqlDBInit()
	fmt.Println(db.GetArticle(2, 10))
}
