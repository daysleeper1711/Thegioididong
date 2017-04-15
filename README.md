# Thegioididong
Fetching data from website www.thegioididong.com

Maven package to excutable file jar
run file jar

The product's data is include the category from mobile, tablet and laptop.

Data will be store in "data" directory inside the working directory.

Sub folder in the "data" directory is divided into 3 directories: mobile, tablet, laptop.

Each product has the json file and image stored in folder with the same name of the product.

Product data is included:
    name, price, image path, specification, manufacturer, category. (in Vietnamese)
Ex:
{
	"name": "iPhone 7 Plus 128GB",
	"category": "mobile",
	"manufacturer": "Apple",
	"price": 22190000,
	"imgPath": "image path here",
	"unit": "VND",
	"specification": {
		"Màn hình": "LED-backlit IPS LCD, 5.5inch, Retina HD",
		"Hệ điều hành": "iOS 10",
		"Camera sau": "Hai camera 12 MP",
		"Camera trước": "7 MP",
		"CPU": "Apple A10 Fusion 4 nhân 64-bit",
		"RAM": "3 GB",
		"Bộ nhớ trong": "128 GB",
		"Thẻ nhớ": "Không",
		"Thẻ SIM": "1 Nano SIM",
		"Dung lượng pin": "2900 mAh"
	}
}