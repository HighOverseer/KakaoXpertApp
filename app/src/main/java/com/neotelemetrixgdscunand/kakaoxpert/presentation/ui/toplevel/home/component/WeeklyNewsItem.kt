package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component

data class WeeklyNewsItem(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val date: String
)

fun getDummyWeeklyNewsItems(): List<WeeklyNewsItem> {
    val imageUrlItems = listOf(
        "https://akcdn.detik.net.id/visual/2023/10/13/tanaman-kakao_169.jpeg?w=715&q=90",
        "https://www.imperial.ac.uk/ImageCropToolT4/imageTool/uploaded-images/newseventsimage_1532078065374_mainnews2012_x1.jpg",
        "https://www.aljazeera.com/wp-content/uploads/2024/03/2024-03-28T130347Z_255422067_RC2AA6A6179P_RTRMADP_3_WESTAFRICA-COCOA-1711805084.jpg?resize=1170%2C780&quality=80",
        "https://cdn.antaranews.com/cache/1200x800/2022/08/26/57.jpg",
        "https://media.licdn.com/dms/image/C5612AQHo76PC6o_LDA/article-cover_image-shrink_600_2000/0/1520206892358?e=2147483647&v=beta&t=I1vQIi48Be_aRFZmczBH5Qu_vfpndL7fNda89_uGibQ"

    )

    return listOf(
        WeeklyNewsItem(
            id = 0,
            title = "Chocolate Faces Shortages, Cocoa Prices Soar by 98% in 3 Months",
            imageUrl = imageUrlItems[0],
            date = "March 22, 2024",
        ),
        WeeklyNewsItem(
            id = 1,
            title = "Definition, Symptoms, and Types of Diseases in Plant Growth",
            imageUrl = imageUrlItems[1],
            date = "May 10, 2024",
        ),
        WeeklyNewsItem(
            id = 2,
            title = "Cocoa Shortages in West Africa",
            imageUrl = imageUrlItems[2],
            date = "September 21, 2024",
        ),
        WeeklyNewsItem(
            id = 3,
            title = "Indonesia's Cocoa Industry Faces Challenges",
            imageUrl = imageUrlItems[3],
            date = "October 2, 2024",
        ),
        WeeklyNewsItem(
            id = 4,
            title = "Cocoa Farmers in Indonesia Struggling with Climate Change and Pest Infestations",
            imageUrl = imageUrlItems[4],
            date = "October 14, 2024",
        ),
        WeeklyNewsItem(
            id = 5,
            title = "Chocolate Faces Shortages, Cocoa Prices Soar by 98% in 3 Months",
            imageUrl = imageUrlItems[0],
            date = "March 22, 2024",
        ),
        WeeklyNewsItem(
            id = 6,
            title = "Definition, Symptoms, and Types of Diseases in Plant Growth",
            imageUrl = imageUrlItems[1],
            date = "May 10, 2024",
        ),
        WeeklyNewsItem(
            id = 7,
            title = "Cocoa Shortages in West Africa",
            imageUrl = imageUrlItems[2],
            date = "September 21, 2024",
        ),
        WeeklyNewsItem(
            id = 8,
            title = "Indonesia's Cocoa Industry Faces Challenges",
            imageUrl = imageUrlItems[3],
            date = "October 2, 2024",
        ),
        WeeklyNewsItem(
            id = 9,
            title = "Cocoa Farmers in Indonesia Struggling with Climate Change and Pest Infestations",
            imageUrl = imageUrlItems[4],
            date = "October 14, 2024",
        ),
    )

}
