from aiogram import Router, F
from aiogram.types import Message, InlineKeyboardMarkup
from aiogram.filters import CommandStart

from keyboards.builders import main_markup
from services.user_service import IUserService

router = Router(name="common")

@router.message(CommandStart())
async def handle_start(message: Message, user_service: IUserService):
    await message.answer("Open", reply_markup=main_markup())


    # user = await user_service.register_user(
    #     telegram_id=message.from_user.id,
    #     chat_id=message.chat.id,
    #     username=message.from_user.username or ""
    # )