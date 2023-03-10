package id.finalproject.binar.secondhand.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import id.finalproject.binar.secondhand.model.local.converters.CategoryConverters
import id.finalproject.binar.secondhand.model.local.converters.ProductConverter
import id.finalproject.binar.secondhand.model.local.dao.*
import id.finalproject.binar.secondhand.model.local.entity.*
import id.finalproject.binar.secondhand.model.network.response.GetUserItem

@Database(
    entities = [Product::class, Category::class, Banner::class, Notification::class, ProductSeller::class, History::class, User::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CategoryConverters::class, ProductConverter::class)
abstract class SecondHandDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun bannerDao(): BannerDao
    abstract fun notificationDao(): NotificationDao
    abstract fun productSellerDao(): ProductSellerDao
    abstract fun historyDao(): HistoryDao
    abstract fun userDao(): UserDao
}