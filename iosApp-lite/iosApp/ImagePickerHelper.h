#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface ImagePickerHelper : NSObject <UIImagePickerControllerDelegate, UINavigationControllerDelegate>

- (void)showImagePickerWithSourceType:(UIImagePickerControllerSourceType)sourceType
                      onImageSelected:(void (^)(NSString *imageUrl))onImageSelected
                          onCancelled:(void (^)(void))onCancelled
                              onError:(void (^)(NSString *error))onError;

@end

NS_ASSUME_NONNULL_END
