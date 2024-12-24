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
		return fmt.Errorf("未找到 UID")
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
	result := db.DB.
		Table(articleTableName).
		Where("article_id = ?", article_id).
		First(&articleFromDB)
	if result.Error != nil {
		return nil, result.Error
	}
	if articleFromDB.BelongsToUID != uid {
		return nil, fmt.Errorf("用户无权限")
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

func (db *MysqlDB) ModifyArticle(uid, aid uint, title, article_data string) error {
	// 检查用户是否拥有该文章
	var article Article
	if err := db.DB.
		Where("article_id = ? AND belongsto_uid = ?", aid, uid).
		First(&article).Error; err != nil {
		// 如果查询不到文章，说明该用户没有该文章
		return fmt.Errorf("文章不存在或用户没有权限修改该文章")
	}

	// 如果找到了文章，更新文章内容
	updateFields := map[string]interface{}{}
	if title != "" {
		updateFields["title"] = title
	}
	if article_data != "" {
		updateFields["data_content"] = article_data // 使用数据库中的列名
	}

	// 执行更新操作
	if err := db.DB.
		Model(&Article{}).
		Where("article_id = ?", aid).
		Updates(updateFields).Error; err != nil {
		return err
	}

	return nil
}
