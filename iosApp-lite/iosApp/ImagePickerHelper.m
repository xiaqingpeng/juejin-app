#import "ImagePickerHelper.h"
#import <Photos/Photos.h>

@interface ImagePickerHelper ()
@property (nonatomic, copy) void (^onImageSelected)(NSString *imageUrl);
@property (nonatomic, copy) void (^onCancelled)(void);
@property (nonatomic, copy) void (^onError)(NSString *error);
@property (nonatomic, strong) UIImagePickerController *picker;
@end

@implementation ImagePickerHelper

- (void)showImagePickerWithSourceType:(UIImagePickerControllerSourceType)sourceType
                      onImageSelected:(void (^)(NSString *imageUrl))onImageSelected
                          onCancelled:(void (^)(void))onCancelled
                              onError:(void (^)(NSString *error))onError {
    self.onImageSelected = onImageSelected;
    self.onCancelled = onCancelled;
    self.onError = onError;
    
    NSLog(@"[ImagePickerHelper] showImagePicker called with sourceType: %ld", (long)sourceType);
    
    // Check if source type is available
    if (![UIImagePickerController isSourceTypeAvailable:sourceType]) {
        NSLog(@"[ImagePickerHelper] Source type not available");
        if (onError) {
            onError(@"Image picker source type not available");
        }
        return;
    }
    
    // Create picker
    self.picker = [[UIImagePickerController alloc] init];
    self.picker.sourceType = sourceType;
    self.picker.delegate = self;
    self.picker.allowsEditing = NO;
    
    NSLog(@"[ImagePickerHelper] Picker created, getting root view controller");
    
    // Get root view controller
    dispatch_async(dispatch_get_main_queue(), ^{
        UIViewController *rootViewController = [self getRootViewController];
        if (rootViewController) {
            NSLog(@"[ImagePickerHelper] Presenting picker");
            [rootViewController presentViewController:self.picker animated:YES completion:^{
                NSLog(@"[ImagePickerHelper] Picker presented successfully");
            }];
        } else {
            NSLog(@"[ImagePickerHelper] Cannot get root view controller");
            if (self.onError) {
                self.onError(@"Cannot get root view controller");
            }
        }
    });
}

- (UIViewController *)getRootViewController {
    // Try multiple methods to get the root view controller
    
    // Method 1: Key window
    UIWindow *keyWindow = nil;
    for (UIWindow *window in [UIApplication sharedApplication].windows) {
        if (window.isKeyWindow) {
            keyWindow = window;
            break;
        }
    }
    
    if (keyWindow && keyWindow.rootViewController) {
        NSLog(@"[ImagePickerHelper] Got root VC from key window");
        return [self topViewController:keyWindow.rootViewController];
    }
    
    // Method 2: First window
    if ([UIApplication sharedApplication].windows.count > 0) {
        UIWindow *firstWindow = [UIApplication sharedApplication].windows.firstObject;
        if (firstWindow.rootViewController) {
            NSLog(@"[ImagePickerHelper] Got root VC from first window");
            return [self topViewController:firstWindow.rootViewController];
        }
    }
    
    // Method 3: Connected scenes (iOS 13+)
    if (@available(iOS 13.0, *)) {
        for (UIScene *scene in [UIApplication sharedApplication].connectedScenes) {
            if ([scene isKindOfClass:[UIWindowScene class]]) {
                UIWindowScene *windowScene = (UIWindowScene *)scene;
                for (UIWindow *window in windowScene.windows) {
                    if (window.isKeyWindow && window.rootViewController) {
                        NSLog(@"[ImagePickerHelper] Got root VC from window scene");
                        return [self topViewController:window.rootViewController];
                    }
                }
            }
        }
    }
    
    NSLog(@"[ImagePickerHelper] Failed to get root view controller");
    return nil;
}

- (UIViewController *)topViewController:(UIViewController *)rootViewController {
    if ([rootViewController isKindOfClass:[UINavigationController class]]) {
        UINavigationController *navigationController = (UINavigationController *)rootViewController;
        return [self topViewController:navigationController.visibleViewController];
    }
    if ([rootViewController isKindOfClass:[UITabBarController class]]) {
        UITabBarController *tabBarController = (UITabBarController *)rootViewController;
        return [self topViewController:tabBarController.selectedViewController];
    }
    if (rootViewController.presentedViewController) {
        return [self topViewController:rootViewController.presentedViewController];
    }
    return rootViewController;
}

#pragma mark - UIImagePickerControllerDelegate

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary<UIImagePickerControllerInfoKey,id> *)info {
    NSLog(@"[ImagePickerHelper] Image selected - delegate called");
    
    // Try to get image URL
    NSURL *imageURL = info[UIImagePickerControllerImageURL];
    if (imageURL) {
        NSLog(@"[ImagePickerHelper] Got image URL: %@", imageURL.absoluteString);
        if (self.onImageSelected) {
            self.onImageSelected(imageURL.absoluteString);
        }
        [picker dismissViewControllerAnimated:YES completion:nil];
        return;
    }
    
    // Try reference URL (for photos from library)
    NSURL *referenceURL = info[UIImagePickerControllerReferenceURL];
    if (referenceURL) {
        NSLog(@"[ImagePickerHelper] Got reference URL: %@", referenceURL.absoluteString);
        if (self.onImageSelected) {
            self.onImageSelected(referenceURL.absoluteString);
        }
        [picker dismissViewControllerAnimated:YES completion:nil];
        return;
    }
    
    // Get UIImage and save to temp directory
    UIImage *image = info[UIImagePickerControllerOriginalImage];
    if (image) {
        NSLog(@"[ImagePickerHelper] Got UIImage object, saving to temp directory");
        [self saveImageToTempDirectory:image picker:picker];
        return;
    }
    
    NSLog(@"[ImagePickerHelper] No image found in info dictionary");
    if (self.onError) {
        self.onError(@"No image found");
    }
    [picker dismissViewControllerAnimated:YES completion:nil];
}

- (void)saveImageToTempDirectory:(UIImage *)image picker:(UIImagePickerController *)picker {
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSData *imageData = UIImageJPEGRepresentation(image, 0.8);
        if (imageData) {
            NSString *tempDir = NSTemporaryDirectory();
            NSString *fileName = [NSString stringWithFormat:@"selected_image_%f.jpg", [[NSDate date] timeIntervalSince1970]];
            NSString *filePath = [tempDir stringByAppendingPathComponent:fileName];
            
            NSError *error = nil;
            [imageData writeToFile:filePath options:NSDataWritingAtomic error:&error];
            
            dispatch_async(dispatch_get_main_queue(), ^{
                if (!error) {
                    NSString *fileURL = [NSURL fileURLWithPath:filePath].absoluteString;
                    NSLog(@"[ImagePickerHelper] Saved to temp file: %@", fileURL);
                    if (self.onImageSelected) {
                        self.onImageSelected(fileURL);
                    }
                } else {
                    NSLog(@"[ImagePickerHelper] Failed to save image: %@", error);
                    if (self.onError) {
                        self.onError([NSString stringWithFormat:@"Failed to save image: %@", error.localizedDescription]);
                    }
                }
                [picker dismissViewControllerAnimated:YES completion:nil];
            });
        } else {
            dispatch_async(dispatch_get_main_queue(), ^{
                NSLog(@"[ImagePickerHelper] Failed to convert image to JPEG");
                if (self.onError) {
                    self.onError(@"Failed to convert image to JPEG");
                }
                [picker dismissViewControllerAnimated:YES completion:nil];
            });
        }
    });
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    NSLog(@"[ImagePickerHelper] User cancelled - delegate called");
    if (self.onCancelled) {
        self.onCancelled();
    }
    [picker dismissViewControllerAnimated:YES completion:nil];
}

@end
