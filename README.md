# Image Sorter Application

The purpose of this program is to generate time series data 
from photographs for statistical analysis. The photos to be
rated are presented to the user in random order to help 
blind the analysis.

The **Image Sorter** application is a simple 
JavaFX program designed to help users sort and rate 
images. It automatically scans a directory for images, 
lets users view them, and assign ratings, which are saved 
in a CSV file for future reference.


---

## Features
- Automatically scans a directory for `.jpg` and `.jpeg` images.
- Displays unrated images in a JavaFX `ImageView`.
- Allows users to assign a numerical rating (1-9) to each image.
- Maintains ratings in a CSV file for persistence.
- Displays the count of unrated images.

---

## Installation

### Prerequisites
- Java 11 or higher
- JavaFX SDK
- IntelliJ

### Steps
1. Clone this repository:
   ```bash
   git clone https://github.com/your-repository/image-sorter
   cd image-sorter
   ```

2. Compile and run the application:
   ```bash
   ./gradlew run
   ```

---

## Usage

### Running the Application
1. Start the program.
2. A window will open displaying an image and a text box to input a rating.
3. Use the following buttons:
    - **Submit**: Assign the entered rating to the current image.
    - **Save**: Save the current ratings to `Ratings.csv`.

### CSV File Format
The ratings are saved in `Ratings.csv` in the following format:
```
rating,image_name,date
```
- Rating: Number from 1-9 assigned by user.
 -1 is used to indicate image has not been rated.
- Image Name: Name of each file in the directory.
  Used as a unique identifier in the code.
- Date: Exif date extracted from image.
  Used to accurately time stamp the images.

---

## License
This project is licensed under the MIT License.
