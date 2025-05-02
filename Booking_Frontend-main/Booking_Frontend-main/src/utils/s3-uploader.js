import AWS from 'aws-sdk';

const s3 = new AWS.S3({
  region: '',
  credentials: {
    accessKeyId: '',
    secretAccessKey: ''
  }
});

export const uploadImageToS3 = async (file) => {
  const params = {
    Bucket: 'booking-image-upload-bucket',
    Key: `products/${Date.now()}-${file.name}`,
    Body: file,
    ContentType: file.type

  };

  try {
    const data = await s3.upload(params).promise();
    return data.Location;
  } catch (error) {
    console.error('Error uploading to S3:', error);
    throw error;
  }
};
