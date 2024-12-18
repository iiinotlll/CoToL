package sqldb

import "fmt"

const articleTableName = "articles"

type Article struct {
	AID          uint   `gorm:"primaryKey;column:article_id"`
	BelongsToUID uint   `gorm:"column:belongsto_uid"`
	Title        string `gorm:"column:title"`
	Data         string `gorm:"column:data_content"`
}

func (db *MysqlDB) PostArticle(uid uint, title, article_data string) error {
	// 检查是否有 uid
	var exists bool
	result := db.DB.Table(userTableName).Select("1").Where("user_UID = ?", uid).Limit(1).Find(&exists)
	if result.RowsAffected == 0 {
		return fmt.Errorf("uid not found")
	}

	// 向 DB 中写入 article
	createResult := db.DB.Table(articleTableName).Create(
		&Article{
			BelongsToUID: uid,
			Title:        title,
			Data:         article_data,
		},
	)
	return createResult.Error
}

func (db *MysqlDB) GetArticle(uid, article_id uint) (*Article, error) {
	var articleFromDB Article
	result := db.DB.Table(articleTableName).Where("article_id = ?", article_id).First(&articleFromDB)
	if result.Error != nil {
		return nil, result.Error
	}
	if articleFromDB.BelongsToUID != uid {
		return nil, fmt.Errorf("wrong belongs to BelongsToUID = %d", articleFromDB.BelongsToUID)
	}
	return &articleFromDB, nil
}

func (db *MysqlDB) DelArticle(uid, article_id uint) error {
	result := db.DB.Table(articleTableName).Where("article_id = ? AND belongsto_uid = ?", article_id, uid).Delete(nil)
	return result.Error
}

func (db *MysqlDB) FindAllArticlesAbstracts(uid uint) ([]Article, error) {
	var articles []Article
	// 使用 SQL 查询截取内容
	if err := db.DB.
		Select("article_id, belongsto_uid, title, LEFT(data_content, 20) AS data_content").
		Where("belongsto_uid = ?", uid).
		Find(&articles).Error; err != nil {
		return nil, err
	}
	return articles, nil
}
