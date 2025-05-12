package com.neotelemetrixgdscunand.kakaoxpert.domain.model

data class NotificationItemData(
    val id: Int,
    val exporterName: String,
    val exporterImageUrl: String,
    val message: String,
    val sendDate: String,
    val isAgreed: Boolean,
    val currentAmountFulfilled: Float,
    val totalAmountDemands: Float,
)

fun getNotificationItemDummies(): List<NotificationItemData> {
    return listOf(
        NotificationItemData(
            exporterName = "Anthony Gasing",
            exporterImageUrl = "https://s3-alpha-sig.figma.com/img/7bb8/e06d/7ba6df889cfe83fee1003b145be98bb3?Expires=1737936000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=oO74RgJwXOdzxaU9NVzA4Mt0Rt3VsxbvX2PAkkl7qPo2Sx8-8trQ4ARbZ4LcfBd9SLHVG9jYnyj6~RVY~QAGjUmE4yjExH5GSdZkpe6J8tPPRaRh7o1lGEApTmw9WX7Vh82yz0qioLfk4WKWpKvVEqnHrRM-U-sOCoGe-Vw41Y4~XrOuugCNmJ4DXAB1N7bn8ds1FWg1m1u5PAWZ2-UGc~mJLtBcCzEKZ~oaM35D9xSLRNhRgYx6oiLt~sjdmM0sbi6RqKqRNP-HyumNuRyeYJNp5PH0y6K7WCgdGfH6NghnDapXYtU22eoY9WNbPD6JtbmhZUYl8DZkhEieYb0CvA__",
            id = 1,
            message = "Membutuhkan 10 Ton Kakao dengan varietas Criollo tawaran harga sebesar \$25.000",
            sendDate = "Jan 24",
            isAgreed = false,
            currentAmountFulfilled = 0f,
            totalAmountDemands = 10f
        ),
        NotificationItemData(
            exporterName = "Anthony Gasing",
            exporterImageUrl = "https://s3-alpha-sig.figma.com/img/7bb8/e06d/7ba6df889cfe83fee1003b145be98bb3?Expires=1737936000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=oO74RgJwXOdzxaU9NVzA4Mt0Rt3VsxbvX2PAkkl7qPo2Sx8-8trQ4ARbZ4LcfBd9SLHVG9jYnyj6~RVY~QAGjUmE4yjExH5GSdZkpe6J8tPPRaRh7o1lGEApTmw9WX7Vh82yz0qioLfk4WKWpKvVEqnHrRM-U-sOCoGe-Vw41Y4~XrOuugCNmJ4DXAB1N7bn8ds1FWg1m1u5PAWZ2-UGc~mJLtBcCzEKZ~oaM35D9xSLRNhRgYx6oiLt~sjdmM0sbi6RqKqRNP-HyumNuRyeYJNp5PH0y6K7WCgdGfH6NghnDapXYtU22eoY9WNbPD6JtbmhZUYl8DZkhEieYb0CvA__",
            id = 2,
            message = "Membutuhkan 10 Ton Kakao dengan varietas Criollo tawaran harga sebesar \$25.000",
            sendDate = "Jan 24",
            isAgreed = true,
            currentAmountFulfilled = 0f,
            totalAmountDemands = 10f
        ),
        NotificationItemData(
            exporterName = "Anthony Gasing",
            exporterImageUrl = "https://s3-alpha-sig.figma.com/img/7bb8/e06d/7ba6df889cfe83fee1003b145be98bb3?Expires=1737936000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=oO74RgJwXOdzxaU9NVzA4Mt0Rt3VsxbvX2PAkkl7qPo2Sx8-8trQ4ARbZ4LcfBd9SLHVG9jYnyj6~RVY~QAGjUmE4yjExH5GSdZkpe6J8tPPRaRh7o1lGEApTmw9WX7Vh82yz0qioLfk4WKWpKvVEqnHrRM-U-sOCoGe-Vw41Y4~XrOuugCNmJ4DXAB1N7bn8ds1FWg1m1u5PAWZ2-UGc~mJLtBcCzEKZ~oaM35D9xSLRNhRgYx6oiLt~sjdmM0sbi6RqKqRNP-HyumNuRyeYJNp5PH0y6K7WCgdGfH6NghnDapXYtU22eoY9WNbPD6JtbmhZUYl8DZkhEieYb0CvA__",
            id = 3,
            message = "Membutuhkan 10 Ton Kakao dengan varietas Criollo tawaran harga sebesar \$25.000",
            sendDate = "Jan 24",
            isAgreed = true,
            currentAmountFulfilled = 10f,
            totalAmountDemands = 10f
        ),
    )
}